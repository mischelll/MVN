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
    items: '/mvn/admin/api/users',
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
    `<tr >
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
    <form action = "#" >
        <button class = "btn btn-success" > View </button>
        <button class = "btn btn-warning" > Edit Role </button>
    <button class= "btn btn-danger" > Delete </button>
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
fetch(URLS.items)
    .then(response => response.json())
    .then(items => {
        let result = '';
        items.forEach(item => {
            const itemString = toString(item);
            result += itemString;
        });
        document.getElementById('users-table').innerHTML = result;
        loader.hide();
    });

