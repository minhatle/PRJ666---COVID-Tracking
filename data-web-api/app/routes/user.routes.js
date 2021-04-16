module.exports = (app , upload)  => {
    const users = require("../controllers/user.controller.js");
    var router = require("express").Router();
    console.log(upload);
    // Create a new User
    router.post("/", users.create);

    //retrieve by username and password
    router.post("/login",users.findUser);
  
    // Retrieve all Users
    router.get("/", users.findAll);
  
    // Retrieve a single User with id
    router.get("/:id", users.findOne);

    // Retrieve a single User with id
    router.get("/getUser/:username", users.findUser);
    // Retrieve a single User with id
    router.put("/getUser/:username", users.updateByUser);
    //update with photo
    router.post("/uploadImage/:username", upload.single('image'), users.updateWithImage)

    // Update a User with id
    router.put("/:id", users.update);
  
    // Delete a User with id
    router.delete("/:id", users.delete);
  
    // Create a new User
    router.delete("/", users.deleteAll);
  
    app.use('/api/users', router);
  };