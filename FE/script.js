document.getElementById('seeAllEmployees').addEventListener('click', seeAllEmployees);
document.getElementById('employee-data-back-button').addEventListener('click', goBack);
document.getElementById('addEmployee').addEventListener('click', addEmployee);
let employeeId = null;

function seeAllEmployees() {
    document.getElementById('employee').style.display = 'none';
    document.getElementById('add-employee').style.display = 'none';
    document.getElementById('employee-table-section').style.display = 'flex';
    document.getElementById("add-employee-form").reset();
    hideOptions();

    fetch('http://localhost:8080/employees/all', {
        METHOD: 'GET',
        headers: {
            'Access-Control-Allow-Origin': ''
        }
    })
        .then(response => response.json())
        .then(data => {
            // Curățăm tabelul de datele anterioare
            const tableBody = document.getElementById('employee-table-body');
            tableBody.innerHTML = '';

            // Adăugăm datele noi în tabel
            data.forEach(item => {
                const row = document.createElement('tr');
                row.innerHTML = `
            <td>${item.firstName}</td>
            <td>${item.lastName}</td>
            <td>${item.position}</td>
            <td>${item.projectName}</td>

        `;
                const detailsButtonCell = document.createElement('td');
                const detailsButton = createButtonWithDetails(item);
                detailsButtonCell.appendChild(detailsButton);
                row.appendChild(detailsButtonCell);

                tableBody.appendChild(row);
            });
        })
        .catch(error => console.error('Eroare fetch:', error));
}

function showDetails(user) {
    const modalH1 = document.getElementById('employee-info-h1');
    const modalP = document.getElementById('employee-info-p');

    modalH1.textContent = user.firstName + ' ' + user.lastName + ' - ' + user.position;
    modalP.textContent = user.projectName + ' | ' + user.hireDate + ' | ' + user.salary + ' RON | ' + user.email;

    employeeId = user.id;

    // Afișăm modalul cu detaliile
    const modal = document.getElementById('employee');
    modal.style.display = 'flex';

    // Ascundem tabelul cu angajati
    const table = document.getElementById('employee-table-section');
    table.style.display = 'none';
}

function createButtonWithDetails(user) {
    const button = document.createElement('button');
    button.classList.add('btn');
    button.textContent = 'View';
    button.addEventListener('click', () => {
        showDetails(user);
    });
    return button;
}

function goBack() {
    document.getElementById('employee').style.display = 'none';
    seeAllEmployees();
}

function hideOptions() {
    // Selectează toate optiunile din pagina
    var options = document.querySelectorAll('.employee-options');

    // Ascunde toate optiunile
    for (var i = 0; i < options.length; i++) {
        options[i].style.display = 'none';
    }
}

function showOption(option) {
    // Ascunde optiunile
    hideOptions();

    // Afișează doar optiunea cu specificata în argumentul funcției
    var optToShow = document.getElementById(option);
    if (optToShow) {
        optToShow.style.display = 'flex';
    }
}


function deleteEmployee() {
    const url = `http://localhost:8080/employees/${employeeId}`;

    fetch(url, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Cererea de ștergere nu a putut fi efectuată.');
            }
            console.log(`Angajatul cu ID-ul ${employeeId} a fost ștears cu succes.`);
            alert('Angajatul a fost sters cu succes!');
            seeAllEmployees();
        })
        .catch(error => {
            console.error('Eroare:', error);
        });
}

function allocateProject() {
    const projectId = document.getElementById('project-id-to-allocate').value;

    const url = `http://localhost:8080/employees/${employeeId}/allocate-project/${projectId}`;

    fetch(url, {
        method: 'PUT'
    })
        .then(response => {
            if (!response.ok) {
                alert('Introduceti date valide!');
                throw new Error('Cererea de alocare nu a putut fi efectuată. Datele introduse sunt invalide.');
            }
            alert('Angajatul a fost alocat cu succes proiectului!');
            console.log(`Angajatul cu ID-ul ${employeeId} a fost alocat cu succes proiectului.`);
            seeAllEmployees();
        })
        .catch(error => {
            console.error('Eroare:', error);
        });

    document.getElementById('project-id-to-allocate').value = '';
}

function updateEmployee(){
    
}

function addEmployee() {
    document.getElementById('add-employee').style.display = 'flex';
    document.getElementById("add-employee-form").addEventListener("submit", handleFormSubmit);
    document.getElementById('employee-table-section').style.display = 'none';
    document.getElementById('employee').style.display = 'none';
}

function handleFormSubmit(event) {
    event.preventDefault(); // Evită trimiterea formularului ca cerere HTTP standard.

    const form = event.target;

    const firstName = form.elements["firstName"].value;
    const lastName = form.elements["lastName"].value;
    const position = form.elements["position"].value;
    const email = form.elements["email"].value;
    const hireDate = form.elements["hireDate"].value;
    const salary = form.elements["salary"].value;

    const employeeData = {
        firstName,
        lastName,
        position,
        email,
        hireDate,
        salary
    };

    add(employeeData);
    form.reset();
}

function add(employeeData) {
    const url = "http://localhost:8080/employees/add"; // Înlocuiește cu URL-ul real al serverului

    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(employeeData),
    })
        .then(response => {
            if (!response.ok) {
                alert('Datele introduse sunt invalide!');
                throw new Error("Cerere eșuată!");
            }
            seeAllEmployees();
        })
        .then(data => {
            console.log("Angajat adăugat cu succes:", data);
            // Poți adăuga aici logica suplimentară sau un mesaj de succes pentru utilizator
        })
        .catch(error => {
            console.error("Eroare la adăugarea angajatului:", error);
            // Poți adăuga aici logica pentru tratarea erorilor sau un mesaj de eroare pentru utilizator
        });
}
