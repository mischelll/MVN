
const URLS = {
    products: '/mvn/shop/api/products',
};

const toString = ({
                      id,
                      title,
                      fullName,
                      username,
                      created,
                      price
                  }) => `         
                             <div class="product">
                   
                            <div class="product_content">
                                <div class="product_title">Item: <a th:href="@{/mvn/shop/product(id=${id})}">${title}</a></div>
                                <div class="product_title">Seller: <a href="product.html">${fullName}</a></div>
                                <div class="product_title">Username: <a href="product.html">${username}</a></div>
                                <div class="product_title">Created On: <a href="product.html">${created}</a></div>
                                <div class="product_price">Price: ${price}</div>
                            </div>
                          </div>        
`;

const loader = {
    show: () => {
        $('#page-loader').show();
    },
    hide: () => {
        $('#page-loader').hide();
    },
};


fetch(URLS.products)
    .then(response => response.json())
    .then(products => {
        let result = '';
        products.forEach(product => {
            const itemString = toString(product);
            result += itemString;
        });
      document.getElementById('products-item').innerHTML = result;

    });