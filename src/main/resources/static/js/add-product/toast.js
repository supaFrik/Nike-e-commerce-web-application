// Toast Notifications Module
// Handles showing toast notifications for user feedback

function showToast(message) {
    let toast = document.getElementById('toast');
    if (!toast) {
        toast = document.createElement('div');
        toast.id = 'toast';
        toast.className = 'toast';
        toast.innerHTML = `
            <i class="fas fa-exclamation-circle"></i>
            <p class="toast-text"></p>
            <i class="fas fa-close" id="close-toast"></i>
        `;
        document.body.appendChild(toast);

        toast.querySelector('#close-toast').addEventListener('click', () => {
            toast.classList.remove('active');
        });
    }
    toast.querySelector('.toast-text').textContent = message;
    toast.classList.add('active');
    setTimeout(() => {
        toast.classList.remove('active');
    }, 5000);
}

// Export for global access
window.Toast = {
    show: showToast
};