

module.exports = (mongoose) => {
  var userSchema = require('./user.schema')(mongoose);
  const User = mongoose.model(
    "user",
    userSchema
  );

  return User;
};
