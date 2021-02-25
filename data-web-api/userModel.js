const mongoose = require("mongoose");
const Schema = mongoose.Schema;

// use bluebird promise library with mongoose
mongoose.Promise = require("bluebird");

// define the user schema
const photoSchema = new Schema({
  "userName": {
    type: String,
    unique: true
  },
  "password": String,
  "email": String,
  "address": String,
  "phoneNumber": String,

});

module.exports = mongoose.model(userSchema);