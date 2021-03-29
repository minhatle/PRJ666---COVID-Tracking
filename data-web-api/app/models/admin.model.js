module.exports = (mongoose) => {
    var userSchema = require('./user.schema')(mongoose);
    const Admin = mongoose.model(
      "admin",
      mongoose.Schema(
        {
          userName: String,
          password: String,
          email: String,
          phoneNumber: String,
          businessID: String,
          managerName: String,
          address: String,
          city: String,
          postalCode: String,
          users: [userSchema]
        },
        { timestamps: true }
      )
    );
  
    return Admin;
  };
  