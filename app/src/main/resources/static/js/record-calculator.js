// HH:mm 形式の文字列を分単位の数値に変換するヘルパー関数
function timeStringToMinutes(timeStr) {
    if (!timeStr || timeStr === '00:00') return 0;
    const parts = timeStr.split(':');
    if (parts.length !== 2) return 0;
    const hours = parseInt(parts[0], 10);
    const minutes = parseInt(parts[1], 10);
    return (hours * 60) + minutes;
}

// 分単位の数値を HH:mm 形式の文字列に変換するヘルパー関数
function minutesToTimeString(minutes) {
    if (minutes < 0) return '00:00';
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;
    const hoursStr = String(hours).padStart(2, '0');
    const minutesStr = String(remainingMinutes).padStart(2, '0');
    return `${hoursStr}:${minutesStr}`;
}

document.addEventListener('DOMContentLoaded', () => {
    // ----------------------------------------------------------------------
    // 変数定義
    // ----------------------------------------------------------------------
    const minutesInput = document.getElementById('sumTime');
    const pomodoro = document.getElementById('pomodoro');
    const checkInput = document.getElementById('usesPomodoro');
    const resetButton = document.getElementById('resetButton');

    const timeNotice = document.getElementById('timeToPomodoroNotice');
    const pomodoroNotice = document.getElementById('pomodoroToTimeNotice');

    // ----------------------------------------------------------------------
    // 関数定義
    // ----------------------------------------------------------------------

    /**
     * フォームの状態を初期設定する (READONLY制御)
     */
    function setFormState() {
        const hasStudyTime = timeStringToMinutes(minutesInput.value) > 0;
        const hasPomodoro = pomodoro.value && parseInt(pomodoro.value) > 0;

        // ★ 修正点: 初期状態はすべて解除
        minutesInput.removeAttribute('readonly');
        pomodoro.removeAttribute('readonly');
        
        // 状態に応じた READONLY 設定
        if (checkInput.checked) {
            // チェックON: 学習時間があればポモドーロをロック
            if (hasStudyTime) {
                pomodoro.setAttribute('readonly', true);
            } else {
                // 学習時間がなければポモドーロ入力を許可
                pomodoro.removeAttribute('readonly');
            }
        } 
        
        // ポモドーロ数で計算されている場合 (チェックの有無に関わらず)
        if (hasPomodoro) {
             // ポモドーロ数から学習時間を算出した状態
             minutesInput.setAttribute('readonly', true); // 学習時間をロック
             pomodoro.removeAttribute('readonly'); // ポモドーロ数の変更は許可
        }
    }
    
    /**
     * APIコール後にフィールドを更新する
     * @param data {pureStudyMinutes: number, pomodoro: number}
     * @param changedId 変更されたフィールドのID
     */
    function updateFields(data, changedId) {
        const { pureStudyMinutes, pomodoro: pomodoroCount } = data;
        
        // 学習時間の更新
        if (pureStudyMinutes !== undefined) { 
            minutesInput.value = minutesToTimeString(pureStudyMinutes);
            if (timeNotice) timeNotice.textContent =
                changedId === 'pomodoro' ? ' (自動算出)' : '';
        }

        // ポモドーロ数の更新
        if (pomodoroCount !== undefined && checkInput.checked) {
            pomodoro.value = pomodoroCount;
            if (pomodoroNotice) pomodoroNotice.textContent =
                changedId === 'sumTime' ? ' (自動算出)' : '';
        } else if (changedId === 'usesPomodoro' && !checkInput.checked) {
            // チェックを外した場合はポモドーロをクリア
            pomodoro.value = '';
            pomodoroNotice.textContent = '';
        }
        
        // 状態を再設定して、readonlyを確定する
        setFormState();
    }
    
    /**
     * フォーム入力値変更のメインハンドラ
     */
    function handleInputChange(event) {
        const changedFieldId = event.target.id;
        let apiUrl = '/api/calculator/calculate?';
        let shouldCallApi = false;
        
        // UI上の表示をリセット
        timeNotice.textContent = '';
        pomodoroNotice.textContent = '';
        
        // 変更前の状態に基づいて、readonly制御を一旦解除
        minutesInput.removeAttribute('readonly');
        pomodoro.removeAttribute('readonly');

        // 1. チェックボックスの変更の処理
        if (changedFieldId === 'usesPomodoro') {
            
            if (checkInput.checked) {
                // チェックON: 学習時間があればポモドーロ算出を強制実行
                const pureStudyMinutes = timeStringToMinutes(minutesInput.value);
                if (pureStudyMinutes > 0) {
                    apiUrl = `/api/calculator/time-to-pomodoro?pureStudyMinutes=${pureStudyMinutes}`;
                    shouldCallApi = true;
                } else {
                    // 学習時間がなければポモドーロ入力を有効化
                    setFormState();
                }
            } else {
                // チェックOFF: ポモドーロ数の値をクリアし、状態をリセット
                pomodoro.value = '';
                setFormState();
            }
            if (!shouldCallApi) return;
        }

        // 2. 学習時間 (sumTime) の変更の処理
        else if (changedFieldId === 'sumTime') {
            const pureStudyMinutes = timeStringToMinutes(minutesInput.value);
            
            // ポモドーロにチェックが入っている場合のみ、ポモドーロ数を算出
            if (checkInput.checked && pureStudyMinutes > 0) {
                 apiUrl = `/api/calculator/time-to-pomodoro?pureStudyMinutes=${pureStudyMinutes}`;
                 shouldCallApi = true;
            } else {
                 // チェックなし、または入力値が0の場合は状態のみ更新
                 pomodoro.value = ''; // 学習時間手入力時はポモドーロをクリア
                 setFormState();
                 return;
            }
        }

        // 3. ポモドーロ数 (pomodoro) の変更の処理
        else if (changedFieldId === 'pomodoro') {
            const pomodoroValue = parseInt(pomodoro.value, 10);
            
            if (pomodoroValue > 0) {
                // ポモドーロ数が入力されたら自動でチェックを入れる
                if (!checkInput.checked) {
                    checkInput.checked = true;
                }
                
                 // ポモドーロ数から学習時間を算出
                 apiUrl = `/api/calculator/pomodoro-to-time?pomodoroCount=${pomodoroValue}`;
                 shouldCallApi = true;
            } else {
                 // 値がクリアされた場合、学習時間をクリアし、状態をリセット
                 minutesInput.value = '00:00';
                 setFormState();
                 return;
            }
        }
        
        // 4. APIコールの実行
        if (shouldCallApi) {
            fetch(apiUrl)
                .then(response => {
                    if (!response.ok) {
                        console.error(`API Error Status: ${response.status}`, `URL: ${apiUrl}`);
                        return response.text().then(text => { throw new Error(`API Error: ${response.status} - ${text}`); });
                    }
                    return response.json();
                })
                .then(data => {
                    updateFields(data, changedFieldId);
                })
                .catch(error => {
                    console.error('計算エラー:', error);
                    timeNotice.textContent = ' (計算エラー)';
                    pomodoroNotice.textContent = ' (計算エラー)';
                    // エラー時はREADONLYを解除し、手動入力を許可
                    minutesInput.removeAttribute('readonly');
                    pomodoro.removeAttribute('readonly');
                });
        }
    }


    // ----------------------------------------------------------------------
    // イベントリスナーの設定
    // ----------------------------------------------------------------------
    
    // sumTime と pomodoro に change イベントを追加
    minutesInput.addEventListener('change', handleInputChange);
    pomodoro.addEventListener('change', handleInputChange);
    
    // usesPomodoro に change イベントを追加
    checkInput.addEventListener('change', handleInputChange);

    resetButton.addEventListener('click', initializeFormState);

    /**
     * フォームを初期状態にリセット（編集画面対応済み）
     */
    function initializeFormState() {
        // ★ 修正点 1: 学習時間を明示的にリセットする
        minutesInput.value = '00:00';
        pomodoro.value = '';
        checkInput.checked = false; // チェックボックスもリセット
        
        timeNotice.textContent = '';
        pomodoroNotice.textContent = '';
        
        // 初期化後、フォームの状態をセットし、readonlyを確定する
        setFormState();
    }
    
    // 初期化処理: ページロード時に実行 (編集画面での既存値保持のため、値の有無チェックはsetFormStateに任せる)
    if (minutesInput.value === '' || minutesInput.value === null) {
         minutesInput.value = '00:00';
    }
    if (pomodoro.value === '' || pomodoro.value === null) {
         pomodoro.value = '';
    }
    setFormState();
});