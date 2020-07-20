const URLS = {
    posts: '/mvn/admin/api/posts',
};
const loader = {
    show: () => {
        $('#page-loader').show();
    },
    hide: () => {
        $('#page-loader').hide();
    },
};
const toString = ({
                      title,
                      postedOn,
                      author,
                      categories,
                      commentsCount
                  }) =>
    `<tr>
    <td>${title} </td>
    <td>${postedOn} </td>
    <td>${author} </td>
    <td>${categories} </td>
    <td>${commentsCount} </td>
    <td>
    <form action = "#">
        <button class = "btn btn-success"> View </button>
        <button class = "btn btn-warning">Edit</button>
        <button class= "btn btn-danger"> Delete </button>
    </form>
        </td>
</tr><br>
`;

loader.show();
fetch(URLS.posts)
    .then(response => response.json())
    .then(posts => {
        let result = '';
        posts.forEach(post => {
            const itemString = toString(post);
            result += itemString;
        });
        document.getElementById('posts-table').innerHTML = result;
        loader.hide();
    });