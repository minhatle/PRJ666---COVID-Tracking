const db = require("../models");
const Bcrypt = require("bcryptjs");
const Admin = db.admins;

// Create and Save a new User
exports.create = (req, res) => {
  // Validate request
  if (!req.body.userName) {
    res.status(400).send({ message: "Content can not be empty!" });
    return;
  }

  // Create a user
  const admin = new Admin({
    userName: req.body.userName,
    password: Bcrypt.hashSync(req.body.password, 10),
    phoneNumber: req.body.phoneNumber,
    businessID: req.body.businessID,
    managerName: req.body.managerName,
    address: req.body.address,
    city: req.body.city,
    postalCode: req.body.postalCode,
  });

  // Save user in the database
  admin
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

  Admin.find(condition)
    .then((data) => {
      res.send(data);
    })
    .catch((err) => {
      res.status(500).send({
        message: err.message || "Some error occurred while retrieving admins.",
      });
    });
};

// Find a single User with an id
exports.findOne = (req, res) => {
  const id = req.params.id;
  Admin.findById(id)
    .then((data) => {
      if (!data)
        res.status(404).send({ message: "Not found Admin with id " + id });
      else {
        console.log(data);
        res.send(data);
      }
    })
    .catch((err) => {
      res.status(500).send({ message: "Error retrieving Admin with id=" + id });
    });
};

//find a single User with a username
exports.findUser = (req, res) => {
  const userName = req.params.username
    ? req.params.username
    : req.body.userName;
  console.log(req.body);
  Admin.findOne({ userName: userName })
    .then((data) => {
      if (!data) {
        res
          .status(404)
          .send({ message: "Not found Admin with username" + userName });
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

exports.findUserByPostal = (req, res) => {
  const postal = req.params.postalCode
    ? req.params.postalCode
    : req.body.postalCode;

  Admin.findOne({ postalCode: postal })
    .then((data) => {
      if (!data) {
        res
          .status(404)
          .send({ message: "Not found Admin with postal code" + postal });
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

  Admin.findByIdAndUpdate(id, req.body, { useFindAndModify: false })
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

  Admin.findOne({ userName: userName })
    .then((data) => {
      if (!data) {
        res
          .status(404)
          .send({ message: "Not found User with username" + userName });
      } else {
        Admin.findByIdAndUpdate(
          data._id,
          { $push: { users: req.body.users } },
          { useFindAndModify: false }
        )
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

  Admin.findByIdAndRemove(id)
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
  Admin.deleteMany({})
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
