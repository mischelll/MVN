const URLS = {
    products: '/mvn/admin/api/products',
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
                      created,
                      username,
                      views,
                      price,
                      email,
                      categories,
                      telephone
                  }) =>
    `<tr id="${id}">
    <td>${title} </td>
    <td>${price} </td>
    <td>${created} </td>
    <td>${username} </td>
    <td>${telephone} </td>
    <td>${email} </td>
    <td>${categories}</td>
    <td>${views} </td>
    <td>
             <button class = "btn btn-success" onclick="window.location.href='mvn/shop/product?id=${id}'"> View </button>
    </td>
     <td>
             <button class = "btn btn-warning" onclick="window.location.href='mvn/shop/product/edit?id=${id}'"> Edit </button>
    </td>
       <td>
            <form class="delete-product" data-id=${id} action = "/mvn/admin/api/products/delete?id=${id}" method="post">
                <button class= "btn btn-danger"> Delete </button>
            </form>
       </td>
</tr><br>
`;

loader.show();
fetch(URLS.products)
    .then(response => response.json())
    .then(products => {
        let result = '';
        products.forEach(product => {
            const itemString = toString(product);
            result += itemString;
        });
        document.getElementById('products-table').innerHTML = result;
        loader.hide();
    });

$('#products-table').on('submit', '.delete-product', function (ev) {
    if (confirm('Are you sure you want to delete this Product?')) {
        const url = $(this).attr('action');
        loader.show();
        fetch(url, {method: 'post'})
            .then(data => {
                const id = $(this).attr('data-id');
                loader.hide();
                $('#' + id).hide();
                alert("Product deleted!");
            });

        ev.preventDefault();
        return false;
    }else {
        console.log("Declined deletion")
    }
});