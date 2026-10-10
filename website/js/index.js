var currentLang = 'en';
var contentSections = [];
var applyTypeFilter = function() {};

function getCurrentTranslations() {
    var translations = window.translations || {};
    return translations[currentLang] || translations.en || {};
}

function createStructuredCard(item) {
    var current = getCurrentTranslations();
    var card = item.url ? document.createElement('a') : document.createElement('article');
    card.className = 'project-section project-section--card block relative flex h-full flex-col overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm transition duration-200 hover:-translate-y-1 hover:shadow-md';
    card.setAttribute('role', item.url ? 'link' : 'article');
    card.style.setProperty('--project-accent', item.accent || '#505050');

    if (item.url) {
        card.href = item.url;
        card.target = '_blank';
        card.rel = 'noopener noreferrer';
    }

    var visual = document.createElement('div');
    visual.className = 'project-section__visual block flex items-center px-4 py-3 text-white';

    var icon = document.createElement('span');
    icon.className = 'project-section__icon';
    icon.textContent = item.icon || '🔧';

    var badge = document.createElement('span');
    badge.className = 'project-section__badge';
    badge.textContent = item.title || '';

    visual.appendChild(icon);
    visual.appendChild(badge);

    var body = document.createElement('div');
    body.className = 'project-section__body flex flex-1 flex-col gap-3 p-4';

    var title = document.createElement('h3');
    title.className = 'project-section__title text-lg font-semibold text-neutral-900';
    title.textContent = item.title || '';

    var summary = document.createElement('p');
    summary.className = 'project-section__summary text-sm leading-6 text-neutral-700';
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
    tagsWrap.className = 'project-section__tags mt-auto flex flex-wrap gap-2';
    if (Array.isArray(item.tags)) {
        item.tags.forEach(function(tag) {
            var tagEl = document.createElement('span');
            tagEl.className = 'project-section__tag inline-flex rounded-full bg-slate-100 px-2.5 py-1 text-[10px] font-medium uppercase tracking-wide text-slate-700';
            tagEl.textContent = tag;
            tagsWrap.appendChild(tagEl);
        });
    }

    body.appendChild(tagsWrap);
    card.appendChild(visual);
    card.appendChild(body);

    return card;
}

function renderStructuredSection(sectionId, key) {
    var section = document.getElementById(sectionId);
    if (!section) return;

    var current = getCurrentTranslations();
    var data = Array.isArray(window[key + 'Data']) ? window[key + 'Data'] : [];

    section.innerHTML = '';

    var heading = document.createElement('h2');
    heading.className = 'project-section__heading text-lg font-bold text-neutral-800';
    heading.textContent = current[key + '_title'] || key;
    section.appendChild(heading);

    var localizedItems = current[key + '_people'] || [];
    data.forEach(function(item, index) {
        section.appendChild(createStructuredCard(Object.assign({}, item, localizedItems[index] || {})));
    });
}

function renderExperienceSection() {
    renderStructuredSection('experience-section', 'experience');
}

function renderContactSection() {
    var section = document.getElementById('contact-section');
    if (!section) return;

    var current = getCurrentTranslations();
    var data = Array.isArray(window.contactData) ? window.contactData : [];
    var localizedItems = current.contact_people || [];

    section.innerHTML = '';

    var heading = document.createElement('h2');
    heading.className = 'project-section__heading text-lg font-bold text-neutral-800';
    heading.textContent = current.contact_title || 'Contact';
    section.appendChild(heading);

    var cards = document.createElement('div');
    cards.className = 'contact-cards';

    data.forEach(function(item, index) {
        var contact = Object.assign({}, item, localizedItems[index] || {});
        var card = contact.url ? document.createElement('a') : document.createElement('article');
        card.className = 'contact-card';
        card.setAttribute('role', contact.url ? 'link' : 'article');
        card.style.setProperty('--contact-accent', contact.accent || '#505050');

        if (contact.url) {
            card.href = contact.url;
            card.target = contact.url.indexOf('mailto:') === 0 || contact.url.indexOf('tel:') === 0 ? '_self' : '_blank';
            if (card.target === '_blank') {
                card.rel = 'noopener noreferrer';
            }
        }

        var icon = document.createElement('span');
        icon.className = 'contact-card__icon';
        icon.setAttribute('aria-hidden', 'true');
        icon.textContent = contact.icon || '✉️';

        var title = document.createElement('span');
        title.className = 'contact-card__title';
        title.textContent = contact.title || '';

        var summary = document.createElement('span');
        summary.className = 'contact-card__summary';
        summary.textContent = contact.summary || '';

        card.appendChild(icon);
        card.appendChild(title);
        card.appendChild(summary);
        cards.appendChild(card);
    });

    section.appendChild(cards);
}

function renderInterestsSection() {
    var section = document.getElementById('interests-section');
    if (!section) return;

    var current = getCurrentTranslations();
    var data = Array.isArray(window.interestsData) ? window.interestsData : [];
    var localizedItems = current.interests_people || [];

    section.innerHTML = '';

    var heading = document.createElement('h2');
    heading.className = 'project-section__heading text-lg font-bold text-neutral-800';
    heading.textContent = current.interests_title || 'Interests';
    section.appendChild(heading);

    var list = document.createElement('ul');
    list.className = 'interests-list';

    data.forEach(function(item, index) {
        var localizedItem = Object.assign({}, item, localizedItems[index] || {});
        var listItem = document.createElement('li');
        listItem.className = 'interests-list__item';
        listItem.textContent = localizedItem.title || '';

        list.appendChild(listItem);
    });

    section.appendChild(list);
}

function renderEducationSection() {
    renderStructuredSection('education-section', 'education');
}

(function() {
    window.renderProjects = function(projectsToRender) {
        var section = document.getElementById('projects-section');
        var dataToRender = projectsToRender || window.projectData;
        if (section && dataToRender && Array.isArray(dataToRender)) {
            var container = section.parentElement;
            section.innerHTML = '';

            var heading = container.querySelector('.project-section__heading') || document.createElement('h2');
            heading.className = 'project-section__heading text-lg font-bold text-neutral-800';
            heading.textContent = getCurrentTranslations().projects_title || 'Projects';

            if (!heading.parentElement) {
                container.insertBefore(heading, section);
            }

            var current = getCurrentTranslations();
            var localizedProjects = current.project_people || [];
            var gridPlacementClasses = [
                'md:col-start-1 md:row-start-1',
                'md:col-start-2 md:row-start-2',
                'md:col-start-3 md:row-start-3'
            ];
            dataToRender.forEach(function(project, index) {
                var localizedProject = localizedProjects[index] || {};
                var card = project.url ? document.createElement('a') : document.createElement('article');
                card.className = 'project-section project-section--card block relative flex h-full flex-col overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm transition duration-200 hover:-translate-y-1 hover:shadow-md ' + (gridPlacementClasses[index] || '');
                card.setAttribute('role', project.url ? 'link' : 'article');
                card.style.setProperty('--project-accent', project.accent || '#505050');

                if (project.url) {
                    card.href = project.url;
                    card.target = '_blank';
                    card.rel = 'noopener noreferrer';
                }

                var visual = document.createElement('div');
                visual.className = 'project-section__visual flex items-center justify-between rounded-t-2xl px-4 py-3 text-white';

                var icon = document.createElement('span');
                icon.className = 'project-section__icon';
                icon.textContent = project.icon || '🔧';

                var badge = document.createElement('span');
                badge.className = 'project-section__badge';
                badge.textContent = current.project_badge || 'GitHub Repo';

                visual.appendChild(icon);
                visual.appendChild(badge);

                var body = document.createElement('div');
                body.className = 'project-section__body flex flex-1 flex-col gap-3 p-4';

                var title = document.createElement('h3');
                title.className = 'project-section__title text-lg font-semibold text-neutral-900';
                title.textContent = localizedProject.title || project.title;

                var summary = document.createElement('p');
                summary.className = 'project-section__summary text-sm leading-6 text-neutral-700';
                summary.textContent = localizedProject.summary || project.summary;

                var tagsWrap = document.createElement('div');
                tagsWrap.className = 'project-section__tags mt-auto flex flex-wrap gap-2';
                var projectTags = localizedProject.tags || project.tags;
                if (Array.isArray(projectTags)) {
                    projectTags.forEach(function(tag) {
                        var tagEl = document.createElement('span');
                        tagEl.className = 'project-section__tag';
                        tagEl.textContent = tag;
                        tagsWrap.appendChild(tagEl);
                    });
                }

                body.appendChild(title);
                body.appendChild(summary);
                body.appendChild(tagsWrap);

                card.appendChild(visual);
                card.appendChild(body);
                section.appendChild(card);
            });
        }
    };

    window.renderCards = window.renderProjects;
    window.renderProjects();
    window.renderExperience = renderExperienceSection;
    window.renderContact = renderContactSection;
    window.renderInterests = renderInterestsSection;
    window.renderEducation = renderEducationSection;
    window.renderExperience();
    window.renderEducation();
    window.renderContact();
    window.renderInterests();

    // Language switcher
    var langFilter = document.getElementById('language-filter') || document.querySelector('.language-filter');
    if (langFilter) {
        function updateLanguage() {
            currentLang = langFilter.value;
            var navigation = window.translations[currentLang].navigation;
            var navigationMenu = document.querySelector('.main-navigation nav');
            if (navigationMenu) {
                navigationMenu.setAttribute('aria-label', window.translations[currentLang].navigation_label);
                Object.keys(navigation).forEach(function(key) {
                    var link = navigationMenu.querySelector('.' + key);
                    if (link) {
                        link.textContent = navigation[key];
                    }
                });
            }
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
            var signatureName = document.getElementById('signature-name');
            if (signatureName) {
                signatureName.textContent = window.translations[currentLang].signature_name;
            }
            // Update labels
            var languageLabel = document.querySelector('label[for="language-filter"]');
            if (languageLabel) {
                languageLabel.innerHTML = '<strong>' + window.translations[currentLang].language_label + '</strong>';
            }
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
            window.renderEducation();
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