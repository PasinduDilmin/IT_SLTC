/* ==============================================================================
   SLTC-IT - Frontend Interactive Micro-Script
   ============================================================================== */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Modal Trigger Handling
    const modalOpenButtons = document.querySelectorAll('[data-modal-open]');
    const modalCloseButtons = document.querySelectorAll('[data-modal-close]');
    
    modalOpenButtons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            const modalId = btn.getAttribute('data-modal-open');
            const targetModal = document.getElementById(modalId);
            if (targetModal) {
                targetModal.classList.add('active');
            }
        });
    });

    modalCloseButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const modal = btn.closest('.modal-overlay');
            if (modal) {
                modal.classList.remove('active');
            }
        });
    });

    // Close modal on backdrop click
    document.querySelectorAll('.modal-overlay').forEach(overlay => {
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) {
                overlay.classList.remove('active');
            }
        });
    });

    // 2. Alert Toast Auto-Dismiss
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-10px)';
            setTimeout(() => alert.remove(), 500);
        }, 5000);

        const closeBtn = alert.querySelector('.alert-close');
        if (closeBtn) {
            closeBtn.addEventListener('click', () => alert.remove());
        }
    });

    // 3. Copy to Clipboard for Crypto Tokens / Serial Numbers
    document.querySelectorAll('.copyable').forEach(el => {
        el.style.cursor = 'pointer';
        el.title = 'Click to copy';
        el.addEventListener('click', () => {
            const text = el.getAttribute('data-copy') || el.innerText.trim();
            navigator.clipboard.writeText(text).then(() => {
                const originalHtml = el.innerHTML;
                el.innerHTML = '<i class="fa-solid fa-check"></i> Copied!';
                setTimeout(() => {
                    el.innerHTML = originalHtml;
                }, 1500);
            });
        });
    });

    // 4. Quick Live Table Search Filter (Client-side)
    const quickSearchInput = document.getElementById('tableLiveFilter');
    if (quickSearchInput) {
        quickSearchInput.addEventListener('input', (e) => {
            const query = e.target.value.toLowerCase();
            const rows = document.querySelectorAll('.data-table tbody tr');
            rows.forEach(row => {
                const text = row.innerText.toLowerCase();
                row.style.display = text.includes(query) ? '' : 'none';
            });
        });
    }

    // 5. Pre-fill Edit Modal Data (if data attributes provided)
    document.querySelectorAll('[data-edit-asset]').forEach(btn => {
        btn.addEventListener('click', () => {
            const id = btn.getAttribute('data-id');
            const tag = btn.getAttribute('data-tag');
            const serial = btn.getAttribute('data-serial');
            const model = btn.getAttribute('data-model');
            const status = btn.getAttribute('data-status');
            const campus = btn.getAttribute('data-campus');
            const dept = btn.getAttribute('data-department');
            const location = btn.getAttribute('data-location');

            const editModal = document.getElementById('editAssetModal');
            if (editModal) {
                const form = editModal.querySelector('form');
                if (form) {
                    form.action = '/assets/' + id + '/update';
                }
                const setVal = (selector, val) => {
                    const el = editModal.querySelector(selector);
                    if (el && val) el.value = val;
                };
                setVal('#editAssetTag', tag);
                setVal('#editSerialNumber', serial);
                setVal('#editModel', model);
                setVal('#editStatus', status);
                setVal('#editCampus', campus);
                setVal('#editDepartment', dept);
                setVal('#editLocation', location);

                editModal.classList.add('active');
            }
        });
    });

    // 6. Pre-fill Return Modal Data
    document.querySelectorAll('[data-return-handover]').forEach(btn => {
        btn.addEventListener('click', () => {
            const id = btn.getAttribute('data-id');
            const tag = btn.getAttribute('data-tag');
            const custodian = btn.getAttribute('data-custodian');

            const modal = document.getElementById('returnModal');
            if (modal) {
                const form = modal.querySelector('form');
                if (form) {
                    form.action = '/handovers/' + id + '/return';
                }
                const targetText = modal.querySelector('#returnModalTargetText');
                if (targetText) {
                    targetText.innerText = tag + ' (' + custodian + ')';
                }
                modal.classList.add('active');
            }
        });
    });
});
