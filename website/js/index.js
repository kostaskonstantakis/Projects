var currentLang = 'en';
var contentSections = [];
var applyTypeFilter = function() {};

function getCurrentTranslations() {
    var translations = window.translations || {};
    return translations[currentLang] || translations.en || {};
}

function createStructuredCard(item) {
    var article = document.createElement('article');
    article.className = 'project-section';
    article.setAttribute('role', 'article');
    article.style.setProperty('--project-accent', item.accent || '#505050');

    var visual = document.createElement('div');
    visual.className = 'project-section__visual';

    var icon = document.createElement('span');
    icon.className = 'project-section__icon';
    icon.textContent = item.icon || '🔧';

    var badge = document.createElement('span');
    badge.className = 'project-section__badge';
    badge.textContent = item.title || '';

    visual.appendChild(icon);
    visual.appendChild(badge);

    var body = document.createElement('div');
    body.className = 'project-section__body';

    var title = document.createElement('h3');
    title.className = 'project-section__title';
    title.textContent = item.title || '';

    var summary = document.createElement('p');
    summary.className = 'project-section__summary';
    summary.textContent = item.summary || '';

    body.appendChild(title);

    if (item.when) {
        var when = document.createElement('p');
        when.className = 'project-section__summary';
        when.textContent = item.when;
        body.appendChild(when);
    }

    body.appendChild(summary);

    var tagsWrap = document.createElement('div');
    tagsWrap.className = 'project-section__tags';
    if (Array.isArray(item.tags)) {
        item.tags.forEach(function(tag) {
            var tagEl = document.createElement('span');
            tagEl.className = 'project-section__tag';
            tagEl.textContent = tag;
            tagsWrap.appendChild(tagEl);
        });
    }

    if (item.url) {
        var link = document.createElement('a');
        link.className = 'project-section__link';
        link.href = item.url;
        link.target = '_blank';
        link.rel = 'noopener noreferrer';
        link.textContent = '';
        body.appendChild(tagsWrap);
        body.appendChild(link);
    } else {
        body.appendChild(tagsWrap);
    }

    article.appendChild(visual);
    article.appendChild(body);

    return article;
}

function renderStructuredSection(sectionId, key) {
    var section = document.getElementById(sectionId);
    if (!section) return;

    var current = getCurrentTranslations();
    var data = Array.isArray(window[key + 'Data']) ? window[key + 'Data'] : [];

    if (section.tagName && section.tagName.toLowerCase() === 'details') {
        section.innerHTML = '';

        var summary = document.createElement('summary');
        summary.textContent = current[key + '_title'] || 'Experience';
        section.appendChild(summary);

        var container = document.createElement('div');
        container.className = 'experience-section__content';

        data.forEach(function(item) {
            container.appendChild(createStructuredCard(item));
        });

        section.appendChild(container);
        return;
    }

    section.innerHTML = '';

    var heading = document.createElement('h2');
    heading.className = 'project-section__heading';
    heading.textContent = current[key + '_title'] || key;
    section.appendChild(heading);

    data.forEach(function(item) {
        section.appendChild(createStructuredCard(item));
    });
}

function renderExperienceSection() {
    renderStructuredSection('experience-section', 'experience');
}

function renderContactSection() {
    renderStructuredSection('contact-section', 'contact');
}

function renderInterestsSection() {
    renderStructuredSection('interests-section', 'interests');
}

(function() {
    window.renderProjects = function(projectsToRender) {
        var section = document.getElementById('projects-section');
        var dataToRender = projectsToRender || window.projectData;
        if (section && dataToRender && Array.isArray(dataToRender)) {
            section.innerHTML = '';
            dataToRender.forEach(function(project) {
                var article = document.createElement('article');
                article.className = 'project-section';
                article.setAttribute('role', 'article');
                article.style.setProperty('--project-accent', project.accent || '#505050');

                var visual = document.createElement('div');
                visual.className = 'project-section__visual';

                var icon = document.createElement('span');
                icon.className = 'project-section__icon';
                icon.textContent = project.icon || '🔧';

                var badge = document.createElement('span');
                badge.className = 'project-section__badge';
                badge.textContent = 'GitHub Repo';

                visual.appendChild(icon);
                visual.appendChild(badge);

                var body = document.createElement('div');
                body.className = 'project-section__body';

                var title = document.createElement('h3');
                title.className = 'project-section__title';
                title.textContent = project.title;

                var summary = document.createElement('p');
                summary.className = 'project-section__summary';
                summary.textContent = project.summary;

                var tagsWrap = document.createElement('div');
                tagsWrap.className = 'project-section__tags';
                if (Array.isArray(project.tags)) {
                    project.tags.forEach(function(tag) {
                        var tagEl = document.createElement('span');
                        tagEl.className = 'project-section__tag';
                        tagEl.textContent = tag;
                        tagsWrap.appendChild(tagEl);
                    });
                }

                var link = document.createElement('a');
                link.className = 'project-section__link';
                link.href = project.url;
                link.target = '_blank';
                link.rel = 'noopener noreferrer';
                link.textContent = '';

                body.appendChild(title);
                body.appendChild(summary);
                body.appendChild(tagsWrap);
                body.appendChild(link);

                article.appendChild(visual);
                article.appendChild(body);
                section.appendChild(article);
            });
        }
    };

    window.renderCards = window.renderProjects;
    window.renderProjects();
    window.renderExperience = renderExperienceSection;
    window.renderContact = renderContactSection;
    window.renderInterests = renderInterestsSection;
    window.renderExperience();
    window.renderContact();
    window.renderInterests();

    // Language switcher
    var langFilter = document.getElementById('language-filter') || document.querySelector('.language-filter');
    if (langFilter) {
        function updateLanguage() {
            currentLang = langFilter.value;
            // Update page title
            if (document.getElementById('page-title')) {
                document.getElementById('page-title').textContent = window.translations[currentLang].page_title;
            } else {
                document.title = window.translations[currentLang].page_title;
            }
            // Update header text
            var headerText = document.getElementById('page-header-text');
            if (headerText) {
                headerText.textContent = window.translations[currentLang].header_text;
            }
            // Update synopsis
            var synopsisEl = document.querySelector('.synopsis');
            if (synopsisEl) {
                synopsisEl.innerHTML = window.translations[currentLang].synopsis;
            }
            // Update labels
            var orientationLabel = document.querySelector('label[for="orientation-filter"]');
            if (orientationLabel) {
                orientationLabel.innerHTML = '<strong>' + window.translations[currentLang].orientation_label + '</strong>';
            }
            var typeLabel = document.querySelector('label[for="type-filter"]');
            if (typeLabel) {
                typeLabel.innerHTML = '<strong>' + window.translations[currentLang].type_label + '</strong>';
            }
            // Update options
            var orientationSelect = document.getElementById('orientation-filter');
            if (orientationSelect) {
                var opts = orientationSelect.options;
                for (var i = 0; i < opts.length; i++) {
                    var val = opts[i].value;
                    opts[i].textContent = window.translations[currentLang].orientation_options[val];
                }
            }
            var typeSelect = document.getElementById('type-filter');
            if (typeSelect) {
                var opts = typeSelect.options;
                for (var i = 0; i < opts.length; i++) {
                    var val = opts[i].value;
                    opts[i].textContent = window.translations[currentLang].type_options[val];
                }
            }
            // Update disclaimer
            var disclaimerEl = document.querySelector('.disclaimer');
            if (disclaimerEl) {
                disclaimerEl.innerHTML = '<strong><i>' + window.translations[currentLang].disclaimer + '</i></strong>';
            }
            // Update footer
            var footerEl = document.getElementById('footer-text');
            if (footerEl) {
                footerEl.innerHTML = window.translations[currentLang].footer_text;
            }
            // Update search placeholder
            var searchInput = document.getElementById('search-filter');
            if (searchInput) {
                searchInput.placeholder = window.translations[currentLang].search_placeholder;
            }
            // Update sort label
            var sortLabel = document.querySelector('label[for="sort-filter"]');
            if (sortLabel) {
                sortLabel.innerHTML = '<strong>' + window.translations[currentLang].sort_label + '</strong>';
            }
            // Update sort options
            var sortSelect = document.getElementById('sort-filter');
            if (sortSelect) {
                var opts = sortSelect.options;
                for (var i = 0; i < opts.length; i++) {
                    var val = opts[i].value;
                    opts[i].textContent = window.translations[currentLang].sort_options[val];
                }
            }
            // Update year and age
            updateYearAndAge();
            // Re-render projects and dynamic sections
            window.renderProjects();
            window.renderExperience();
            window.renderContact();
            window.renderInterests();
            // Re-apply filters
            applyTypeFilter();
            // Save to localStorage
            try { localStorage.setItem('language', currentLang); } catch (e) {}
        }
        // Load from localStorage
        try {
            var savedLang = localStorage.getItem('language');
            if (savedLang && window.translations[savedLang]) {
                currentLang = savedLang;
                langFilter.value = savedLang;
            }
        } catch (e) {}
        updateLanguage();
        langFilter.addEventListener('change', updateLanguage);
    }
})();

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

function updateYearAndAge() {
	var yearEl = document.getElementById('year');
	if (yearEl) {
		yearEl.textContent = new Date().getFullYear();
	}
	var ageEl = document.getElementById('age');
	if (ageEl) ageEl.textContent = getAge('1995-02-26');
}

updateYearAndAge();

(function () {
	var filter = document.getElementById('type-filter');
	if (!filter) return;

	applyTypeFilter = function() {
		var value = filter.value;
		contentSections.forEach(function (el) {
			var typeAttr = el.getAttribute('data-type') || '';
			var types = typeAttr.split(/\s+/).filter(Boolean);
			var match = value === 'all' || types.indexOf(value) !== -1;
			el.classList.toggle('type-hidden', !match);
		});
	};
	filter.addEventListener('change', applyTypeFilter);
	applyTypeFilter();
})();