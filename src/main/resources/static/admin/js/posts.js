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
                      id,
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
             <button class = "btn btn-success" onclick="window.location.href='/posts/article?id=${id}'"> View </button>
    </td>
     <td>
               <button class = "btn btn-warning" onclick="window.location.href='/posts/edit?id=${id}'">Edit</button>
      </td>
       <td>
            <form action = "#">
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