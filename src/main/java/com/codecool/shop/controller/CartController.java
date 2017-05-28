package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductDao;
//import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.jdbc.ProductDaoJdbc;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.LineItem;
import org.json.simple.JSONObject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles products in cart
 */
public class CartController {

    private static CartController instance = null;
    private CartController() {}

    /**
     * Singleton constructor for my cart
     * @return CartCategoryJBDC
     */
    public static CartController getInstance() {
        if (instance == null) {
            instance = new CartController();
        }
        return instance;
    }

    /**
     * Handles adding items to cart
     * @param req Product item id.
     * @param res Spark thing.
     * @return JSON for ajax
     */
    public JSONObject addItemToCart(Request req, Response res) {
        int addedProductId = Integer.parseInt(req.params(":id"));
        ProductDao productDataStore = new ProductDaoJdbc();
        Cart cartDataStore = getCart(req);

        LineItem lineItemCandidate = new LineItem(productDataStore.find(addedProductId));
        cartDataStore.add(lineItemCandidate);

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("numberOfProductsInCart", cartDataStore.getAllQuantity());
        res.type("application/json");
        return jsonObj;
    }

    /**
     * Handles cart's things
     * Adds parameters to frontend
     * @param req Cart's containment
     * @param res Spark thing
     * @return Spark ModelAndView
     */
    public ModelAndView renderCart(Request req, Response res) {
        Cart cartDataStore = getCart(req);
        Map<String, Object> params = new HashMap<>();
        params.put("lineitems", cartDataStore.getAll());
        params.put("sum", cartDataStore.getSum());
        return new ModelAndView(params, "cartview");
    }

    /**
     * Gets session parameters to backend
     * @param req gets cart from session
     * @return cart
     */
    public Cart getCart(Request req) {
        Cart cart = req.session().attribute("cart");
        if (cart == null) {
            cart = new Cart();
            req.session().attribute("cart", cart);
        }
        return cart;
    }
}
