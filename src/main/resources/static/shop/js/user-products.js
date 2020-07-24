const URLS = {
    items: '/mvn/admin/api/my-products/',
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
