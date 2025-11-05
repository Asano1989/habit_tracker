document.addEventListener('DOMContentLoaded', () => {
  const hamburgerBtn = document.getElementById('hamburger-btn');
  const sidebarMenu = document.getElementById('sidebar-menu');

  if (!hamburgerBtn || !sidebarMenu) return;

  hamburgerBtn.addEventListener('click', () => {
    sidebarMenu.classList.toggle('active');
  });

  // メニュー内リンククリックで閉じる
  sidebarMenu.querySelectorAll('a').forEach(link => {
    link.addEventListener('click', () => {
      sidebarMenu.classList.remove('active');
    });
  });
});
