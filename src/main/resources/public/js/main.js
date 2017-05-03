/**
 * Created by szarazdenes on 2017. 05. 03..
 */
function cartAdder() {

    $(".add-to-cart").click(function insertToCart() {
        var idToCart = this.getAttribute("id");
        var idProd = idToCart.slice(8);
        alert(idProd);
        toSession(idProd);

    })

}

function toSession(idProd) {
    $.ajax({
        url: '/addtocart/' + idProd,
        type: 'GET',
        success: function () {
            cartAdder()
        },
        error: function () {
            console.log("Error")
        }

    })

}






$(document).ready(function () {
    cartAdder();

});