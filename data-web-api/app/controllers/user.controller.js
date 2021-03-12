const db = require("../models");
const Bcrypt = require("bcryptjs");
const User = db.users;

// Create and Save a new User
exports.create = (req, res) => {
  // Validate request
  if (!req.body.userName) {
    res.status(400).send({ message: "Content can not be empty!" });
    return;
  }

  // Create a user
  const user = new User({
    userName: req.body.userName,
    password: Bcrypt.hashSync(req.body.password, 10),
    email: req.body.email,
    firstName: req.body.firstName,
    lastName: req.body.lastName,
    address: req.body.address,
  });

  // Save user in the database
  user
    .save()
    .then((data) => {
      res.send(data);
    })
    .catch((err) => {
      res.status(500).send({
        message: err.message || "Some error occurred while creating the user.",
      });
    });
};

// Retrieve all Users from the database.
exports.findAll = (req, res) => {
  const userName = req.query.userName;
  var condition = userName
    ? { userName: { $regex: new RegExp(userName), $options: "i" } }
    : {};

  User.find(condition)
    .then((data) => {
      res.send(data);
    })
    .catch((err) => {
      res.status(500).send({
        message: err.message || "Some error occurred while retrieving users.",
      });
    });
};

// Find a single User with an id
exports.findOne = (req, res) => {
  const id = req.params.id;
  User.findById(id)
    .then((data) => {
      if (!data)
        res.status(404).send({ message: "Not found User with id " + id });
      else {
        console.log(data);
        res.send(data);
      }
    })
    .catch((err) => {
      res.status(500).send({ message: "Error retrieving User with id=" + id });
    });
};

//find a single User with a username
exports.findUser = (req, res) => {
  const userName = req.params.username
    ? req.params.username
    : req.body.userName;

  User.findOne({ userName: userName })
    .then((data) => {
      if (!data) {
        res
          .status(404)
          .send({ message: "Not found User with username" + userName });
      } else if (
        req.body.password &&
        !Bcrypt.compareSync(req.body.password, data.password)
      ) {
        return res.status(400).send({ message: "The password is invalid" });
      } else {
        res.send(data);
      }
    })
    .catch((err) => {
      res
        .status(500)
        .send({ message: "Error retrieving User with username" + userName });
    });
};

// Update a User by the id in the request
exports.update = (req, res) => {
  if (!req.body) {
    return res.status(400).send({
      message: "Data to update can not be empty!",
    });
  }

  const id = req.params.id;

  User.findByIdAndUpdate(id, req.body, { useFindAndModify: false })
    .then((data) => {
      if (!data) {
        res.status(404).send({
          message: `Cannot update user with id=${id}. Maybe user was not found!`,
        });
      } else res.send({ message: "User was updated successfully." });
    })
    .catch((err) => {
      res.status(500).send({
        message: "Error updating user with id=" + id,
      });
    });
};

exports.updateByUser = (req, res) => {
  if (!req.body) {
    return res.status(400).send({
      message: "Data to update can not be empty!",
    });
  }

  const userName = req.params.username;

  User.findOne({ userName: userName })
    .then((data) => {
      if (!data) {
        res
          .status(404)
          .send({ message: "Not found User with username" + userName });
      } else {
        User.findByIdAndUpdate(data._id, req.body, { useFindAndModify: false })
          .then((data) => {
            if (!data) {
              res.status(404).send({
                message: `Cannot update user with id=${data._id}. Maybe user was not found!`,
              });
            } else res.send({ message: "User was updated successfully." });
          })
          .catch((err) => {
            res.status(500).send({
              message: "Error updating user with id=" + data._id,
            });
          });
        res.send(data);
      }
    })
    .catch((err) => {
      res
        .status(500)
        .send({ message: "Error retrieving User with username" + userName });
    });
};

// Delete a User with the specified id in the request
exports.delete = (req, res) => {
  const id = req.params.id;

  User.findByIdAndRemove(id)
    .then((data) => {
      if (!data) {
        res.status(404).send({
          message: `Cannot delete user with id=${id}. Maybe user was not found!`,
        });
      } else {
        res.send({
          message: "User was deleted successfully!",
        });
      }
    })
    .catch((err) => {
      res.status(500).send({
        message: "Could not delete user  with id=" + id,
      });
    });
};

// Delete all Users from the database.
exports.deleteAll = (req, res) => {
  User.deleteMany({})
    .then((data) => {
      res.send({
        message: `${data.deletedCount} Users were deleted successfully!`,
      });
    })
    .catch((err) => {
      res.status(500).send({
        message: err.message || "Some error occurred while removing all users.",
      });
    });
};
