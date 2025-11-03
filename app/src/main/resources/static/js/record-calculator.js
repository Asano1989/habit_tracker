// HH:mm 形式の文字列を分単位の数値に変換するヘルパー関数
function timeStringToMinutes(timeStr) {
    if (!timeStr || timeStr === '00:00') return 0;
    const parts = timeStr.split(':');
    if (parts.length !== 2) return 0;
    const hours = parseInt(parts[0], 10);
    const minutes = parseInt(parts[1], 10);
    return (hours * 60) + minutes;
}

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
    // 変数定義 (すべての要素をここで取得し、nullエラーを回避)
    // ----------------------------------------------------------------------
    const startInput = document.getElementById('startTime');
    const stopInput = document.getElementById('stopTime');
    const breakInput = document.getElementById('breakTime');
    const minutesInput = document.getElementById('sumTime');
    const pomodoroInput = document.getElementById('pomodoroInput');
    const checkInput = document.getElementById('usesPomodoro');
    const resetButton = document.getElementById('resetButton');

    // ★ 通知要素もここで取得 (nullエラー対策)
    const timeNotice = document.getElementById('timeToPomodoroNotice');
    const pomodoroNotice = document.getElementById('pomodoroToTimeNotice');

    const timeInputGroup = document.querySelectorAll('.time-input-group');
    const manualInputGroup = [minutesInput, pomodoroInput];

    const timeFields = [startInput, stopInput, breakInput];
    // 排他制御のための監視フィールド (checkInputは排他制御のトリガーではないため含めない)
    const primaryInputFields = [...timeFields, minutesInput, pomodoroInput]; 
    const allFields = [...primaryInputFields, checkInput]; // イベントリスナー全体用


    // ----------------------------------------------------------------------
    // 関数定義 (すべての関数は DOMContentLoaded のクロージャ内に配置)
    // ----------------------------------------------------------------------

    /**
     * 排他制御を設定するヘルパー関数
     * @param {string} activeGroup - 'time' または 'manual'
     */
    function setExclusiveLock(activeGroup) {
        if (activeGroup === 'time') {
            // Start/Stop/Break がアクティブ
            manualInputGroup.forEach(input => input.setAttribute('readonly', true));
            timeInputGroup.forEach(input => input.removeAttribute('readonly'));
        } else if (activeGroup === 'manual') {
            // sumTime / pomodoroInput がアクティブ
            timeInputGroup.forEach(input => input.setAttribute('readonly', true));
            manualInputGroup.forEach(input => input.removeAttribute('readonly'));
        }
    }
    
    /**
     * APIレスポンスに基づいてフォームフィールドを更新
     */
    function updateFields(data, changedId) {
        const { pureStudyMinutes, pomodoroCount } = data;
        
        // 純学習時間フィールドを更新
        if (pureStudyMinutes !== undefined && minutesInput.id !== changedId) {
            minutesInput.value = minutesToTimeString(pureStudyMinutes);
            // ★ 通知要素の変数を使用
            if (timeNotice) timeNotice.textContent = 
                changedId === 'sumTime' ? '' : ' (自動計算)';
        }

        // ポモドーロ数フィールドを更新 (チェックが入っている場合のみ表示/更新)
        if (pomodoroCount !== undefined && pomodoroInput.id !== changedId) {
            pomodoroInput.value = pomodoroCount;
            // ★ 通知要素の変数を使用
            if (pomodoroNotice) pomodoroNotice.textContent = 
                changedId === 'pomodoroInput' ? '' : ' (自動計算)';
        }
    };
    
    /**
     * フォーム入力と排他制御の状態をリセットし、初期状態に戻す
     */
    function initializeFormState() {
        // 1. 全フィールドの readonly 属性を解除 (値のクリアのために必要)
        [...timeInputGroup, ...manualInputGroup].forEach(input => input.removeAttribute('readonly'));
        
        // 2. フォームの値をクリア (開始/終了/休憩時間もクリアします)
        startInput.value = '';
        stopInput.value = '';
        breakInput.value = '00:00'; // type="time" の初期値
        minutesInput.value = '00:00'; // type="time" の初期値
        pomodoroInput.value = '';
        checkInput.checked = false; // チェックボックスもクリア
        
        // 3. 通知メッセージをクリア
        if (timeNotice) timeNotice.textContent = '';
        if (pomodoroNotice) pomodoroNotice.textContent = '';
        
        // 4. 初期状態: すべてのフィールドを入力可能な状態に戻す (排他制御なし)
        // ロック関数を呼ばず、すべて解除した状態を初期とします。
    }

    /**
     * 入力値が変更された際のメインハンドラ
     */
    function handleInputChange(event) {
        const changedFieldId = event.target.id;
        const changedValue = event.target.value;
        const isPomodoroChecked = checkInput.checked;
        let apiUrl = '/api/calculator/calculate?';

        // チェックボックスの変更は単独では計算しない
        if (changedFieldId === 'usesPomodoro') {
            // チェックを切り替えたら、最新の値で再計算を行う
            if(minutesInput.value !== '00:00') {
                // 学習時間があれば、それに合わせてポモドーロ数を再計算
                handleInputChange({ target: minutesInput });
            }
            return;
        }
        
        // -----------------------------------------------------
        // 排他制御の呼び出しとAPIリクエストパターンの決定
        // -----------------------------------------------------
        let shouldCallApi = false;

        if (timeFields.some(f => f.id === changedFieldId) && changedValue) {
            // 1. Start, Stop, Break のいずれかが入力された場合
            if (!startInput.value || !stopInput.value) return; // 値が不完全なら計算しない
            setExclusiveLock('time');
            apiUrl += `startTime=${startInput.value}&stopTime=${stopInput.value}&breakTime=${breakInput.value}`;
            shouldCallApi = true;

        } else if (changedFieldId === 'sumTime' && changedValue) {
            // 2. 純学習時間（分）が変更された場合
            setExclusiveLock('manual');
            const pureStudyMinutes = timeStringToMinutes(minutesInput.value);
            if (pureStudyMinutes === 0) return;
            
            // ★ ポモドーロ計算が不要な場合でも、学習時間からポモドーロ数を算出させるAPI設計に依存
            apiUrl += `pureStudyMinutes=${pureStudyMinutes}`;
            shouldCallApi = true;

        } else if (changedFieldId === 'pomodoroInput' && changedValue) {
            // 3. ポモドーロ数が変更された場合

            // ★ 追記: 0または空の場合にリセット処理を行う
            const pomodoroValue = parseInt(pomodoroInput.value, 10);
            if (isNaN(pomodoroValue) || pomodoroValue <= 0) {
                // ポモドーロ数が0または空欄の場合、フォーム全体を初期状態にリセット
                initializeFormState();
                return; // リセット後はAPI呼び出しをしない
            }

            setExclusiveLock('manual');
            apiUrl += `pomodoroCount=${pomodoroInput.value}`;
            shouldCallApi = true;
        }

        if (shouldCallApi) {
            fetch(apiUrl)
                .then(response => {
                    // APIエラー (500) 対策: 成功ステータスのみ続行
                    if (!response.ok) {
                        throw new Error(`API Error: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    // ★ チェックが入っていなければ、pomodoroInputは更新しないように制御
                    const finalData = { ...data };
                    if (!isPomodoroChecked) {
                        finalData.pomodoroCount = undefined; // ポモドーロ数を更新対象から除外
                        if (pomodoroNotice) pomodoroNotice.textContent = '';
                    }
                    updateFields(finalData, changedFieldId);
                })
                .catch(error => {
                    console.error('計算エラー:', error);
                    // エラー時の処理
                    if (timeNotice) timeNotice.textContent = ' (計算エラー)';
                    if (pomodoroNotice) pomodoroNotice.textContent = ' (計算エラー)';
                });
        }
    };


    // ----------------------------------------------------------------------
    // イベントリスナーの設定
    // ----------------------------------------------------------------------
    
    // 全フィールドに変更イベントを設定
    allFields.forEach(field => {
        // ★ nullチェックを追加し、エラーを回避
        if (field) {
            field.addEventListener('change', handleInputChange);
        } else {
            console.error(`Missing element for ID: ${field.id}`);
        }
    });

    // リセットボタンにイベントを設定
    resetButton.addEventListener('click', initializeFormState); // ★ リセット関数名を変更

    // 初期化処理: ページロード時に実行
    initializeFormState();
});