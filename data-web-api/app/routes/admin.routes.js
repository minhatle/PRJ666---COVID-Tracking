module.exports = app => {
    const admins = require("../controllers/admin.controller.js");
    
    var router = require("express").Router();
  
    // Create a new admin
    router.post("/", admins.create);

    //retrieve by adminname and password
    router.post("/login",admins.findUser);
  
    // Retrieve all Users
    router.get("/", admins.findAll);
  
    // Retrieve a single User with id
    router.get("/:id", admins.findOne);

    // Retrieve a single User with id
    router.get("/getAdmin/:username", admins.findUser);
    // Retrieve a single User with id
    router.put("/getAdmin/:username", admins.updateByUser);

    router.get("/postalCode",admins.findUserByPostal)
    router.post("/postalCode",admins.findUserByPostal)

    router.get("/phoneNumber",admins.findUserByPhone)
    router.post("/phoneNumber",admins.findUserByPhone)

    // Update a User with id
    router.put("/:id", admins.update);
  
    // Delete a User with id
    router.delete("/:id", admins.delete);
  
    // Create a new User
    router.delete("/", admins.deleteAll);
  
    app.use('/api/admin', router);
  };