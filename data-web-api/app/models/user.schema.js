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
      covidStatus: String, 
      questionnaire: { symptoms: Boolean, contact: Boolean, travel: Boolean, date:String},
      img: { 
        data: Buffer, 
        contentType: String 
     },
     businesses: [{userName: String, date: String}]
    },
    { timestamps: true }
  );
  return userSchema
};
