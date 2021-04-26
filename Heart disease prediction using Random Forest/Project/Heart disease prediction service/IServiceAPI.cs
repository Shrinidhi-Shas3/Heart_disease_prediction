using System.Data;

namespace JSONWebAPI
{
    public interface IServiceAPI
    {

        string ALogin(string adminId, string password);
        string getDoctors();
        string AddDoc(string Name, string Address, string cont, string email, string age, string gender, string cate);
        string UpdateDoc(string did, string Name, string Address, string cont
      , string email, string age, string gender, string cate);
        string DelDoc(string did);
        string getUsers();
        string getFeedback();
        string getTrainingData();

        string AddTrainingData(string DName, string Age, string gender, string chestpain, string bloodsugar
          , string restecg, string exang, string ca, string slope, string thal, string bloodpressure
          , string cholesterol, string thalach, string oldpeak);

        string UpdateTrainingData(string tid, string DName, string Age, string gender, string chestpain, string bloodsugar
          , string restecg, string exang, string ca, string slope, string thal, string bloodpressure
          , string cholesterol, string thalach, string oldpeak);

        string DeleteTraningData(string tid);

        //-------------------User Functions------------------------------------------------------------------
        string Register(string name, string gender, string age, string address, string cont, string email, string pass);
        string PLogin(string email, string pass);

        string PgetProfile(string pid);
        string PUpdateProfile(string id, string name, string gender, string age, string address, string city, string state, string cont, string email);
        string PChangePass(string id, string oldpass, string newpass);
        string AnalyseHeart(string uid, string chestpain, string bloodsugar, string restecg, string exang,
            string ca, string slope, string thal, string bloodpressure, string cholesterol, string thalach, string oldpeak);
        //getDoctors()
        string searchDoctor(string src, string query);
        string AddFeedBack(string uid, string feedback, string date, string time);
        string PgetFeedback(string uid);
        
    }
}