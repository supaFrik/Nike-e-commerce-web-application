(function () {
    const body = document.body;
    if (!body || !body.classList.contains('order-page-body')) {
        return;
    }

    document.querySelectorAll('.action-link.disabled').forEach((link) => {
        link.setAttribute('aria-disabled', 'true');
        link.addEventListener('click', (event) => {
            event.preventDefault();
        });
    });

    const currentTimelineStep = document.querySelector('.timeline-step.current');
    if (currentTimelineStep) {
        currentTimelineStep.setAttribute('aria-current', 'step');
    }

    document.querySelectorAll('.history-item.success .history-card').forEach((card) => {
        card.setAttribute('data-payment-state', 'success');
    });
})();
