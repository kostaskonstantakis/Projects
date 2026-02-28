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
	var splideInstance = null;
	var originalHTML = null;

	function destroySplide() {
		if (splideInstance) {
			splideInstance.destroy();
			splideInstance = null;
		}
	}

	function initSplide() {
		destroySplide();
		
		// store original HTML if not already done
		if (!originalHTML) {
			originalHTML = section.innerHTML;
		}
		
		// clear and build Splide structure
		section.innerHTML = '';
		section.classList.add('splide');
		section.setAttribute('aria-label', 'Guitar Carousel');
		
		// create track
		var track = document.createElement('div');
		track.className = 'splide__track';
		
		// create list
		var list = document.createElement('ul');
		list.className = 'splide__list';
		
		// collect all visible guitar sections from original HTML
		var temp = document.createElement('div');
		temp.innerHTML = originalHTML;
		var items = temp.querySelectorAll('.guitar-section');
		var slideCount = 0;
		
		items.forEach(function (item) {
			if (!item.classList.contains('type-hidden')) {
				var li = document.createElement('li');
				li.className = 'splide__slide';
				
				// extract just the img-wrap for cleaner slides
				var imgWrap = item.querySelector('.guitar-section__img-wrap');
				if (imgWrap) {
					var wrap = imgWrap.cloneNode(true);
					wrap.style.position = 'relative';
					wrap.style.display = 'flex';
					wrap.style.alignItems = 'center';
					wrap.style.justifyContent = 'center';
					li.appendChild(wrap);
					list.appendChild(li);
					slideCount++;
				}
			}
		});
		
		track.appendChild(list);
		section.appendChild(track);
		
		// create pagination
		var pagination = document.createElement('ul');
		pagination.className = 'splide__pagination';
		section.appendChild(pagination);
		
		// create arrows
		var arrows = document.createElement('div');
		arrows.className = 'splide__arrows';
		var prevBtn = document.createElement('button');
		prevBtn.className = 'splide__arrow splide__arrow--prev';
		prevBtn.type = 'button';
		prevBtn.setAttribute('aria-label', 'Previous slide');
		prevBtn.textContent = '❮';
		var nextBtn = document.createElement('button');
		nextBtn.className = 'splide__arrow splide__arrow--next';
		nextBtn.type = 'button';
		nextBtn.setAttribute('aria-label', 'Next slide');
		nextBtn.textContent = '❯';
		arrows.appendChild(prevBtn);
		arrows.appendChild(nextBtn);
		section.appendChild(arrows);
		
		if (slideCount === 0) {
			console.warn('No visible guitar sections found for slideshow');
			return;
		}
		
		try {
			splideInstance = new Splide(section, {
				type: 'fade',
				perPage: 1,
				rewind: true,
				arrows: 'slider',
				pagination: 'slider'
			}).mount();
			console.log('Splide mounted with ' + slideCount + ' slides');
		} catch (e) {
			console.error('Splide initialization failed:', e);
		}
	}

	function restoreGrid() {
		destroySplide();
		if (originalHTML) {
			section.innerHTML = originalHTML;
			section.classList.remove('splide');
		}
	}

	function applyOrientation() {
		var value = filter.value;
		var typeFilterControl = document.querySelector('.type-filter-control');
		
		if (value === 'slideshow') {
			initSplide();
			section.classList.remove('orientation-vertical', 'orientation-horizontal');
			section.classList.add('orientation-slideshow');
			// hide type filter in slideshow
			if (typeFilterControl) {
				typeFilterControl.style.display = 'none';
			}
		} else {
			if (section.classList.contains('orientation-slideshow')) {
				restoreGrid();
			}
			section.classList.remove('orientation-slideshow');
			section.classList.remove('orientation-horizontal', 'orientation-vertical');
			section.classList.add('orientation-' + value);
			// show type filter in grid modes
			if (typeFilterControl) {
				typeFilterControl.style.display = '';
			}
		}
		try { localStorage.setItem(STORAGE_KEY, value); } catch (e) {}
	}

	try {
		var saved = localStorage.getItem(STORAGE_KEY);
		if (saved === 'horizontal' || saved === 'vertical' || saved === 'slideshow') {
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
		
		// rotate if the main gallery itself is in horizontal mode
		var gallery = document.getElementById('guitars-section');
		if (gallery && gallery.classList.contains('orientation-horizontal')) {
			lightbox.classList.add('orientation-horizontal');
		} else {
			lightbox.classList.remove('orientation-horizontal');
		}
		
		lightbox.hidden = false;
		if (closeBtn) closeBtn.focus();
		document.body.style.overflow = 'hidden';
	}

	function closeLightbox() {
		lightbox.hidden = true;
		lightboxImg.removeAttribute('src');
		lightbox.classList.remove('orientation-horizontal');
		document.body.style.overflow = '';
	}

	document.querySelectorAll('.guitar-section').forEach(function (section) {
		var img = section.querySelector('img');
		if (!img) return;
		section.addEventListener('click', function (e) {
			e.preventDefault();
			// toggle overlay on click
			section.classList.toggle('active');
			// remove active class from siblings
			document.querySelectorAll('.guitar-section.active').forEach(function (el) {
				if (el !== section) el.classList.remove('active');
			});
			openLightbox(img.src, section.getAttribute('data-description') || img.alt);
		});
	});
	
	// close overlay when clicking elsewhere
	document.addEventListener('click', function (e) {
		if (!e.target.closest('.guitar-section')) {
			document.querySelectorAll('.guitar-section.active').forEach(function (section) {
				section.classList.remove('active');
			});
		}
	});

	if (closeBtn) closeBtn.addEventListener('click', closeLightbox);
	lightbox.addEventListener('click', function (e) {
		if (e.target === lightbox) closeLightbox();
	});
	document.addEventListener('keydown', function (e) {
		if (e.key === 'Escape' && !lightbox.hidden) closeLightbox();
	});
})();