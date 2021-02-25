module.exports = mongoose => {
    const User = mongoose.model(
      "user",
      mongoose.Schema(
        {
          userName: String,
          password: String,
          email: String,
          firstName: String, 
          lastName: String, 
          address: String
        },
        { timestamps: true }
      )
    );
  
    return User;
  };