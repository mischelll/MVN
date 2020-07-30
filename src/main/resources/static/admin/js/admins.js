const URLS = {
    users: '/mvn/admin/api/admins',
};
const toString = ({
                      firstName,
                      lastName,
                      username,
                      email,
                      registeredOn,
                      active
                  }) => {
    let columns =
        `
    <td>${firstName} </td>
    <td>${lastName} </td>
    <td>${username} </td>
    <td>${email} </td>
    <td>${registeredOn} </td>
    <td> ${active}</td>
   
    `;

    return `<tr id="${username}" >${columns}</tr>`;
};


const loader = {
    show: () => {
        $('#page-loader').show();
    },
    hide: () => {
        $('#page-loader').hide();
    },
};

loader.show();
fetch(URLS.users)
    .then(response => response.json())
    .then(users => {
        let result = '';
        users.forEach(user => {
            const itemString = toString(user);
            result += itemString;
        });
        document.getElementById('admins-table').innerHTML = result;
        loader.hide();
    });