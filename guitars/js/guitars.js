var currentYear = new Date().getFullYear();
document.getElementById('year').textContent = currentYear;

function getAge(birthDate) {
	var today = new Date();
	var birth = new Date(birthDate);
	var age = today.getFullYear() - birth.getFullYear();
	var monthDiff = today.getMonth() - birth.getMonth();
	if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
		age--;
	}
	return age;
}

var ageEl = document.getElementById('age');
if (ageEl) ageEl.textContent = getAge('1995-02-26');

/* Orientation: εφαρμογή + αποθήκευση στο localStorage */
(function () {
	var filter = document.getElementById('orientation-filter');
	var section = document.getElementById('guitars-section');
	if (!filter || !section) return;

	var STORAGE_KEY = 'guitars-orientation';

	function applyOrientation() {
		var value = filter.value;
		section.classList.remove('orientation-horizontal', 'orientation-vertical');
		section.classList.add('orientation-' + value);
		try { localStorage.setItem(STORAGE_KEY, value); } catch (e) {}
	}

	try {
		var saved = localStorage.getItem(STORAGE_KEY);
		if (saved === 'horizontal' || saved === 'vertical') {
			filter.value = saved;
		}
	} catch (e) {}
	applyOrientation();
	filter.addEventListener('change', applyOrientation);
})();

/* Φίλτρο τύπου (electric / bass / acoustic) */
(function () {
	var filter = document.getElementById('type-filter');
	var sections = document.querySelectorAll('.guitar-section[data-type]');
	if (!filter || !sections.length) return;

	function applyTypeFilter() {
		var value = filter.value;
		sections.forEach(function (el) {
			var typeAttr = el.getAttribute('data-type') || '';
			var types = typeAttr.split(/\s+/).filter(Boolean);
			var match = value === 'all' || types.indexOf(value) !== -1;
			el.classList.toggle('type-hidden', !match);
		});
	}
	filter.addEventListener('change', applyTypeFilter);
	applyTypeFilter();
})();

/* Lightbox: κλικ σε εικόνα κιθάρας για μεγέθυνση */
(function () {
	var lightbox = document.getElementById('lightbox');
	var lightboxImg = lightbox && lightbox.querySelector('.lightbox__img');
	var lightboxCaption = lightbox && lightbox.querySelector('.lightbox__caption');
	var closeBtn = lightbox && lightbox.querySelector('.lightbox__close');
	if (!lightbox || !lightboxImg) return;

	function openLightbox(src, caption) {
		lightboxImg.src = src;
		lightboxImg.alt = caption || '';
		lightboxCaption.textContent = caption || '';
		lightbox.hidden = false;
		if (closeBtn) closeBtn.focus();
		document.body.style.overflow = 'hidden';
	}

	function closeLightbox() {
		lightbox.hidden = true;
		lightboxImg.removeAttribute('src');
		document.body.style.overflow = '';
	}

	document.querySelectorAll('.guitar-section').forEach(function (section) {
		var img = section.querySelector('img');
		if (!img) return;
		section.addEventListener('click', function (e) {
			e.preventDefault();
			openLightbox(img.src, section.getAttribute('data-description') || img.alt);
		});
	});

	if (closeBtn) closeBtn.addEventListener('click', closeLightbox);
	lightbox.addEventListener('click', function (e) {
		if (e.target === lightbox) closeLightbox();
	});
	document.addEventListener('keydown', function (e) {
		if (e.key === 'Escape' && !lightbox.hidden) closeLightbox();
	});
})();