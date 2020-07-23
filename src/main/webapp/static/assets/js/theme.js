// Custome theme code

if ($('.clean-gallery').length > 0) {
   baguetteBox.run('.clean-gallery', { animation: 'slideIn'});
}

if ($('.clean-product').length > 0) {
    $(window).on("load",function() {
        $('.sp-wrap').smoothproducts();
    });
}


// STUDENT CODE
const url_api = 'http://localhost:8888/';
const url_shopping_cart = url_api + 'shopping-cart';

async function updateQuantity() {
    let prodQuantity = this.value;
    let prodId = this.getAttribute("data-prod-id");

    let dataToBePosted = {
        'quantity': prodQuantity,
        'prodId': prodId
    }
    let serverResponse = await fetch(url_shopping_cart, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(dataToBePosted)
    });
    let textResponse = await serverResponse.text();
    window.location.reload();
}

let quantityInputs = document.getElementsByClassName("form-control quantity-input");
for (let quantityInput of quantityInputs) {
    quantityInput.addEventListener("click", updateQuantity);
}

// console.log("AICI");

