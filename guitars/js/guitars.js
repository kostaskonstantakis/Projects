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

(function () {
    var filter = document.getElementById('orientation-filter');
    var section = document.getElementById('guitars-section');
    if (!filter || !section) return;

    function applyOrientation() {
        var value = filter.value;
        section.classList.remove('orientation-horizontal', 'orientation-vertical');
        section.classList.add('orientation-' + value);
    }

    filter.addEventListener('change', applyOrientation);
    applyOrientation();
})();