(function (Drupal, drupalSettings) {
  'use strict';

  // ========== DARK MODE ==========
  function initDarkMode() {
    const darkModeKey = 'guitars-dark-mode';
    const btn = document.getElementById('dark-mode-toggle');
    function setDark(isDark) {
      document.documentElement.setAttribute('data-theme', isDark ? 'dark' : 'light');
      if (btn) btn.textContent = isDark ? '🌙' : '☀️';
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
      // Avoid attaching multiple times
      if (!btn.hasAttribute('data-dark-mode-initialized')) {
        btn.setAttribute('data-dark-mode-initialized', 'true');
        btn.addEventListener('click', function () {
          isDark = !isDark;
          setDark(isDark);
        });
      }
    }
  }

  // ========== ORIENTATION (vertical / horizontal / slideshow) ==========
  function initOrientation() {
    var filter = document.getElementById('orientation-filter');
    var section = document.getElementById('guitars-section');
    var STORAGE_KEY = 'guitars-orientation';
    if (!filter || !section) return;
    function applyOrientation() {
      var value = filter.value || 'vertical';
      // καθαρίζουμε προηγούμενες κλάσεις
      section.classList.remove('orientation-vertical', 'orientation-horizontal', 'orientation-slideshow');
      section.classList.add('orientation-' + value);
      try {
        localStorage.setItem(STORAGE_KEY, value);
      } catch (e) { }
    }
    // restore από localStorage
    try {
      var saved = localStorage.getItem(STORAGE_KEY);
      if (saved === 'horizontal' || saved === 'vertical' || saved === 'slideshow') {
        filter.value = saved;
      }
    } catch (e) { }
    applyOrientation();
    filter.addEventListener('change', applyOrientation);
  }

  // ========== TYPE FILTER (All / Electric / Bass / Acoustic / Signature / Baritone / 7string) ==========
  function initTypeFilter() {
    var filter = document.getElementById('type-filter');
    var section = document.getElementById('guitars-section');
    if (!filter || !section) return;
    function applyTypeFilter() {
      var value = filter.value || 'all';
      var cards = section.querySelectorAll('.guitar-section[data-type]');
      cards.forEach(function (el) {
        var typeAttr = el.getAttribute('data-type') || '';
        var types = typeAttr.split(/\s+/).filter(Boolean);
        var match = (value === 'all') || (types.indexOf(value) !== -1);
        el.classList.toggle('type-hidden', !match);
      });
    }
    filter.addEventListener('change', applyTypeFilter);
    applyTypeFilter();
  }

  // ========== LIGHTBOX ==========
  function initLightbox() {
    var lightbox = document.getElementById('lightbox');
    if (!lightbox) return;
    var lightboxImg = lightbox.querySelector('.lightbox__img');
    var lightboxCaption = lightbox.querySelector('.lightbox__caption');
    var closeBtn = lightbox.querySelector('.lightbox__close');
    var gallery = document.getElementById('guitars-section');
    if (!lightboxImg || !gallery) return;

    if (lightbox.hasAttribute('data-lightbox-initialized')) {
      return;
    }
    lightbox.setAttribute('data-lightbox-initialized', 'true');

    function openLightbox(src, caption) {
      lightboxImg.src = src;
      lightboxImg.alt = caption || '';
      if (lightboxCaption) {
        lightboxCaption.textContent = caption || '';
      }
      // Αν το gallery είναι οριζόντιο, περιστρέφουμε και το lightbox (προαιρετικό)
      if (gallery.classList.contains('orientation-horizontal')) {
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