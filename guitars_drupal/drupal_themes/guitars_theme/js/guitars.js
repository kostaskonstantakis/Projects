(function() {
	'use strict';

	// ========== DARK MODE ==========
	function initDarkMode() {
		const darkModeKey = 'guitars-dark-mode';
		const btn = document.getElementById('dark-mode-toggle');
		
		function setDark(isDark) {
			document.documentElement.setAttribute('data-theme', isDark ? 'dark' : 'light');
			if (btn) {
				btn.textContent = isDark ? '☀️' : '🌙';
				btn.setAttribute('aria-pressed', isDark ? 'true' : 'false');
				btn.setAttribute('aria-label', isDark ? 'Switch to light mode' : 'Switch to dark mode');
			}
			try { localStorage.setItem(darkModeKey, isDark ? 'true' : 'false'); } catch (e) { }
		}
		
		let isDark = false;
		try {
			const saved = localStorage.getItem(darkModeKey);
			if (saved !== null) {
				isDark = saved === 'true';
			}
		} catch (e) { }
		
		setDark(isDark);
		
		if (btn) {
			btn.addEventListener('click', function() {
				isDark = !isDark;
				setDark(isDark);
			});
		}
	}

	// ========== ORIENTATION ==========
	function initOrientation() {
		const filter = document.getElementById('orientation-filter');
		const section = document.getElementById('guitars-section');
		const STORAGE_KEY = 'guitars-orientation';
		
		if (!filter || !section) return;
		
		function applyOrientation() {
			const value = filter.value || 'vertical';
			section.classList.remove('orientation-vertical', 'orientation-horizontal', 'orientation-slideshow');
			section.classList.add('orientation-' + value);
			try {
				localStorage.setItem(STORAGE_KEY, value);
			} catch (e) { }
		}
		
		try {
			const saved = localStorage.getItem(STORAGE_KEY);
			if (saved === 'horizontal' || saved === 'vertical' || saved === 'slideshow') {
				filter.value = saved;
			}
		} catch (e) { }
		
		applyOrientation();
		filter.addEventListener('change', applyOrientation);
	}

	// ========== TYPE FILTER ==========
	function initTypeFilter() {
		const filter = document.getElementById('type-filter');
		const section = document.getElementById('guitars-section');
		
		if (!filter || !section) return;
		
		function applyTypeFilter() {
			const value = filter.value || 'all';
			const cards = section.querySelectorAll('.guitar-section[data-type]');
			
			cards.forEach(function(el) {
				const typeAttr = el.getAttribute('data-type') || '';
				const types = typeAttr.split(/\s+/).filter(Boolean);
				const match = (value === 'all') || (types.indexOf(value) !== -1);
				el.classList.toggle('type-hidden', !match);
			});
		}
		
		filter.addEventListener('change', applyTypeFilter);
		applyTypeFilter();
	}

	// ========== LIGHTBOX ==========
	function initLightbox() {
		const lightbox = document.getElementById('lightbox');
		if (!lightbox) return;
		
		const lightboxImg = lightbox.querySelector('.lightbox__img');
		const lightboxCaption = lightbox.querySelector('.lightbox__caption');
		const closeBtn = lightbox.querySelector('.lightbox__close');
		const gallery = document.getElementById('guitars-section');
		let previousFocus = null;
		let focusableElements = [];
		
		if (!lightboxImg || !gallery) return;

		function updateFocusableElements() {
			focusableElements = Array.prototype.slice.call(lightbox.querySelectorAll('button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'))
				.filter(function (el) { return !el.disabled && el.offsetParent !== null; });
		}

		function trapFocus(event) {
			if (event.key !== 'Tab') return;
			updateFocusableElements();
			if (!focusableElements.length) return;
			const first = focusableElements[0];
			const last = focusableElements[focusableElements.length - 1];
			if (event.shiftKey) {
				if (document.activeElement === first || document.activeElement === lightbox) {
					event.preventDefault();
					last.focus();
				}
			} else {
				if (document.activeElement === last) {
					event.preventDefault();
					first.focus();
				}
			}
		}

		function openLightbox(src, caption) {
			previousFocus = document.activeElement;
			lightboxImg.src = src;
			lightboxImg.alt = caption || '';
			if (lightboxCaption) {
				lightboxCaption.textContent = caption || '';
			}
			
			if (gallery.classList.contains('orientation-horizontal')) {
				lightbox.classList.add('orientation-horizontal');
			} else {
				lightbox.classList.remove('orientation-horizontal');
			}
			
			lightbox.hidden = false;
			updateFocusableElements();
			if (closeBtn) {
				closeBtn.focus();
			} else if (focusableElements.length) {
				focusableElements[0].focus();
			}
			document.addEventListener('keydown', trapFocus, true);
			document.body.style.overflow = 'hidden';
		}

		function closeLightbox() {
			lightbox.hidden = true;
			document.body.style.overflow = '';
			document.removeEventListener('keydown', trapFocus, true);
			if (previousFocus && previousFocus.focus) {
				previousFocus.focus();
			}
		}

		const guitarSections = gallery.querySelectorAll('.guitar-section');
		guitarSections.forEach(function(section) {
			section.addEventListener('click', function(e) {
				if (e.target.tagName === 'IMG' || e.target.closest('.guitar-section__img-wrap')) {
					const img = section.querySelector('img');
					const caption = section.getAttribute('data-description') || '';
					if (img) {
						openLightbox(img.src, caption);
					}
				}
			});
		});

		document.addEventListener('click', function(e) {
			if (!e.target.closest('.guitar-section') && !e.target.closest('.lightbox__inner')) {
				closeLightbox();
			}
		});

		if (closeBtn) closeBtn.addEventListener('click', closeLightbox);
		
		lightbox.addEventListener('click', function(e) {
			if (e.target === lightbox) closeLightbox();
		});
		
		document.addEventListener('keydown', function(e) {
			if (e.key === 'Escape' && !lightbox.hidden) closeLightbox();
		});
	}

	// ========== INITIALIZATION ==========
	document.addEventListener('DOMContentLoaded', function() {
		initDarkMode();
		initOrientation();
		initTypeFilter();
		initLightbox();
	});

})();
        lightbox.classList.add('orientation-horizontal');
      } else {
        lightbox.classList.remove('orientation-horizontal');
      }
      lightbox.hidden = false;
      document.body.style.overflow = 'hidden';
      if (closeBtn) closeBtn.focus();
    }
    function closeLightbox() {
      lightbox.hidden = true;
      lightboxImg.removeAttribute('src');
      lightbox.classList.remove('orientation-horizontal');
      document.body.style.overflow = '';
    }
    // event delegation: click σε οποιαδήποτε guitar-section
    gallery.addEventListener('click', function (e) {
      var card = e.target.closest('.guitar-section');
      if (!card) return;
      var img = card.querySelector('img');
      if (!img) return;
      e.preventDefault();
      openLightbox(img.src, card.getAttribute('data-description') || img.alt || '');
    });
    if (closeBtn) {
      closeBtn.addEventListener('click', function (e) {
        e.preventDefault();
        closeLightbox();
      });
    }
    // click έξω από την εικόνα
    lightbox.addEventListener('click', function (e) {
      if (e.target === lightbox) {
        closeLightbox();
      }
    });
    // Escape κλείνει το lightbox
    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' && !lightbox.hidden) {
        closeLightbox();
      }
    });
  }

  // ========== DRUPAL BEHAVIOR ==========
  Drupal.behaviors.guitarsBehavior = {
    attach: function (context, settings) {
      // τρέχουμε μόνο μία φορά ανά σελίδα, αλλά με check του context
      // Το init σκοπεύεται κυρίως στο root document
      if (context !== document) {
        return;
      }
      initDarkMode();
      initOrientation();
      initTypeFilter();
      initLightbox();
    }
  };
})(Drupal, drupalSettings);