// <tr>
// <td>Jill</td>
// <td>Smith</td>
// <td>50</td>
// <td>50</td>
// <td>50</td>
// <td>50</td>
// <td>50</td>
// <td>50</td>
// <td>50</td>
// <td>50</td>
// <td>
// <form action="#">
//     <button class="btn btn-success">View</button>
//     <button class="btn btn-warning">Edit Role</button>
// <button class="btn btn-danger">Delete</button>
//     </form>
//     </td>
//     </tr>

const URLS = {
    users: '/mvn/admin/api/users',
};
const toString = ({
                      firstName,
                      lastName,
                      username,
                      email,
                      roles,
                      registeredOn,
                      gender,
                      postsCount,
                      active
                  }) =>
    `<tr id="${username}" >
    <td>${firstName} </td>
    <td>${lastName} </td>
    <td>${username} </td>
    <td>${email} </td>
    <td>${registeredOn} </td>
    <td>${roles} </td>
    <td>${gender} </td>
    <td>${postsCount} </td>
    <td> 50 </td>
    <td> 50 </td>
    <td> ${active}</td>
    <td >
    <button class = "btn btn-success" onclick="window.location.href='/mvn/users/api/profile-view/${username}'"  > View </button>
    </td>
    <td>
     <form action = "#" >
    <button class = "btn btn-warning" > Edit Role </button>
    </form>
    </td>
    <td>
    <form class="delete-user" data-id=${username} action="/mvn/admin/api/users/delete/${username}" method="post">
        <button class= "btn btn-danger"  > Delete </button>
    </form>
        </td>
</tr><br>
`;

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
        document.getElementById('users-table').innerHTML = result;
        loader.hide();
    });

$('#users-table').on('submit', '.delete-user', function (ev) {
    if (confirm('Are you sure you want to save this thing into the database?')) {
        // Save it!
        console.log('Thing was saved to the database.');

        const url = $(this).attr('action');
        loader.show();
        fetch(url, {method: 'post'})
            .then(data => {
                const id = $(this).attr('data-id');
                // console.log(id);
                loader.hide();

                $('#' + id).hide();


            })
        ev.preventDefault();
        return false;
    }
});

