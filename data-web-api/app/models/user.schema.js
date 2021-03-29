module.exports = (mongoose) => {
  const userSchema = mongoose.Schema(
    {
      userName: String,
      password: String,
      email: String,
      firstName: String,
      lastName: String,
      address: String,
      city: String,
      postalCode: String,
      questionnaire: { symptoms: Boolean, contact: Boolean, travel: Boolean },
    },
    { timestamps: true }
  );
  return userSchema
};
