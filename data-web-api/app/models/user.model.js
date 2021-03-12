module.exports = (mongoose) => {
  const User = mongoose.model(
    "user",
    mongoose.Schema(
      {
        userName: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String,
        address: String,
        questionnaire: { symptoms: Boolean, contact: Boolean, travel: Boolean },
      },
      { timestamps: true }
    )
  );

  return User;
};
