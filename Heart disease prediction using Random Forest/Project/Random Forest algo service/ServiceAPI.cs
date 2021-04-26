using System;
using System.Data;
using System.Data.SqlClient;
using System.Collections.Generic;

namespace JSONWebAPI
{
    public class ServiceAPI : IServiceAPI
    {
        SqlConnection con = new SqlConnection(@"Data Source=43.255.152.25;Initial Catalog=AHeartRandom;Persist Security Info=True;User ID=AHeartRandom;Password=ls*30W9z;Trusted_Connection=True");
        SqlCommand cmd;
        SqlDataReader dr;
        JSONMaker jm = new JSONMaker();

        public ServiceAPI()
        {
            
        }


        //-------------------Admin Part------------------------------------------------------------------

        public string ALogin(string adminId, string password) {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select * from Admin where AdminId='" + adminId + "' AND Password='" + password + "'", con);
                DataSet ds = new DataSet();
                da.Fill(ds);
                int count = ds.Tables[0].Rows.Count;
                if (count > 0)
                {
                    //pid or did
                    ans = jm.Singlevalue("true");
                }
                else
                {
                    //false
                    ans = jm.Singlevalue("false");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }
            return ans;
        }

        public string getDoctors()
        {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select * from DocDetails order by Name", con);
                DataSet ds = new DataSet();
                da.Fill(ds);
                if (ds.Tables[0].Rows.Count > 0)
                {
                    //did,name,add,mobile,email, age, gender, category
                    ans = jm.Maker(ds);
                }
                else
                {
                    //no
                    ans = jm.Singlevalue("no");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }
            return ans;
        }

        public string AddDoc(string Name, string Address, string cont, string email, string age, string gender, string cate)
        {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select * from DocDetails where Email='" + email + "'", con);
                DataSet ds = new DataSet();
                da.Fill(ds);

                if (ds.Tables[0].Rows.Count > 0)
                {
                    //already
                    ans = jm.Singlevalue("already");
                }
                else
                {
                    cmd = new SqlCommand("insert into DocDetails values('" + Name + "','" + Address + "','" + cont + "','" + email + "','" + age + "','" + gender + "','" + cate + "')", con);
                    con.Open();
                    cmd.ExecuteNonQuery();
                    con.Close();

                    ans = jm.Singlevalue("true");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }

            return ans;
        }

        public string UpdateDoc(string did, string Name, string Address, string cont, string email, string age, string gender, string cate)
        {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select * from DocDetails where Email='" + email + "' AND Did<>'" + did + "'", con);
                DataSet ds = new DataSet();
                da.Fill(ds);

                if (ds.Tables[0].Rows.Count > 0)
                {
                    //already
                    ans = jm.Singlevalue("already");
                }
                else
                {
                    cmd = new SqlCommand("update DocDetails set Name='" + Name + "',Address='" + Address + "',Mobile='" + cont 
                        + "',Email='" + email + "',Age='" + age + "',Gender='" + gender + "',Specialist='"+ cate
                        + "' where Did='" + did + "'", con);
                    con.Open();
                    cmd.ExecuteNonQuery();
                    con.Close();

                    ans = jm.Singlevalue("true");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }

            return ans;
        }

        public string DelDoc(string did)
        {
            string ans = "";
            try
            {
                cmd = new SqlCommand("Delete from DocDetails where Did='" + did + "'", con);
                con.Open();
                cmd.ExecuteNonQuery();
                con.Close();

                ans = jm.Singlevalue("true");
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }
            return ans;
        }

        public string getUsers()
        {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select * from Register order by Name", con);
                DataSet ds = new DataSet();
                da.Fill(ds);
                if (ds.Tables[0].Rows.Count > 0)
                {
                    //Name,Address, Mobile, Email, Age, Gender, Password
                    ans = jm.Maker(ds);
                }
                else
                {
                    //no
                    ans = jm.Singlevalue("no");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }
            return ans;
        }

        public string getFeedback()
        {
            string ans = "";
            try
            { 
                SqlDataAdapter da = new SqlDataAdapter("select (select Name from Register where id = f.Uid),f.Feedback,f.Date,f.Time from Feedback f order by f.Uid DESC", con);
                DataSet ds = new DataSet();
                da.Fill(ds);
                if (ds.Tables[0].Rows.Count > 0)
                {
                    //name, feed,date,time
                    ans = jm.Maker(ds);
                }
                else
                {
                    //no
                    ans = jm.Singlevalue("no");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }
            return ans;
        }

        public string getTrainingData ()
        {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select * from TrainingData order by DName", con);
                DataSet ds = new DataSet();
                da.Fill(ds);
                if (ds.Tables[0].Rows.Count > 0)
                {
                    //tid, Dname,Age,Gender,ChestPain,Blood Sugar, Restecg, Exang, Slope, CA, Thal
                    //, BloodPressure, Cholesterol, Thalach, OldPeak
                    ans = jm.Maker(ds);
                }
                else
                {
                    //no
                    ans = jm.Singlevalue("no");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }
            return ans;
        }

        public string AddTrainingData(string DName, string Age, string gender, string chestpain, string bloodsugar
            , string restecg, string exang, string ca, string slope, string thal, string bloodpressure
            , string cholesterol, string thalach, string oldpeak)
        {
            string ans = "";
            try
            {
                cmd = new SqlCommand("insert into TrainingData values('" + DName + "','" + Age + "','" + gender + "','" + chestpain
                    + "','" + bloodsugar + "','" + restecg + "','" + exang+ "','" + ca + "','" + slope
                    + "','" + thal + "','" + bloodpressure + "','" + cholesterol
                    + "','" + thalach + "','" + oldpeak + "')", con);
                con.Open();
                cmd.ExecuteNonQuery();
                con.Close();

                ans = jm.Singlevalue("true");
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }

            return ans;
        }

        public string UpdateTrainingData (string tid, string DName, string Age, string gender, string chestpain, string bloodsugar
            , string restecg, string exang, string ca, string slope, string thal, string bloodpressure
            , string cholesterol, string thalach, string oldpeak)
        {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select * from TrainingData where Tid ='" + tid + "'", con);
                DataSet ds = new DataSet();
                da.Fill(ds);

                if (ds.Tables[0].Rows.Count > 0)
                {
                    cmd = new SqlCommand("update TrainingData set DName='" + DName + "',Age='" + Age + "',Gender='" + gender
                        + "',ChestPain='" + chestpain + "',BloodSugar='" + bloodsugar + "',Restecg='" + restecg + "',Exang='" + exang
                        + "',Slope='" + slope + "',CA='" + ca + "',Thal='" + thal + "',BloodPressure='" + bloodpressure
                        + "',Cholesterol='" + cholesterol + "',Thalach='" + thalach + "',Oldpeak='" + oldpeak
                        + "' where Tid='" + tid + "'", con);
                    con.Open();
                    cmd.ExecuteNonQuery();
                    con.Close();

                    ans = jm.Singlevalue("true");
                    
                }
                else
                {
                    //already
                    ans = jm.Singlevalue("false");

                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }

            return ans;
        }

        public string DeleteTraningData(string tid)
        {
            string ans = "";
            try
            {
                cmd = new SqlCommand("Delete from TrainingData where tid ='" + tid + "'", con);
                con.Open();
                cmd.ExecuteNonQuery();
                con.Close();

                ans = jm.Singlevalue("true");
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }
            return ans;
        }
        


        //-------------------User Functions------------------------------------------------------------------

        public string Register(string name, string gender, string age, string address, string cont, string email, string pass)
        {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select * from Register where Email='" + email + "'", con);
                DataSet ds = new DataSet();
                da.Fill(ds);

                if (ds.Tables[0].Rows.Count > 0)
                {
                    //already
                    ans = jm.Singlevalue("already");
                }
                else
                {
                    cmd = new SqlCommand("insert into Register values('" + name + "','" + address + "','" + cont + "','" + email + "','" 
                        + age + "','" + gender + "','" + pass + "')", con);
                    con.Open();
                    cmd.ExecuteNonQuery();
                    con.Close();

                    ans = jm.Singlevalue("true");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }

            return ans;
        }

        public string PLogin(string email, string pass)
        {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select Id from Register where Email='" + email + "' AND Password='" + pass + "'", con);
                DataSet ds = new DataSet();
                da.Fill(ds);
                int count = ds.Tables[0].Rows.Count;
                if (count > 0)
                {
                    //id
                    ans = jm.Maker(ds);
                }
                else
                {
                    //false
                    ans = jm.Singlevalue("false");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }
            return ans;

        }

        public string PgetProfile(string pid)
        {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select Id,Name,Gender,Age,Address,Mobile,Email from Register where Id='" + pid + "'", con);
                DataSet ds = new DataSet();
                da.Fill(ds);
                if (ds.Tables[0].Rows.Count > 0)
                {
                    //Id, name, gender, age, address, mobile, email
                    ans = jm.Maker(ds);
                }
                else
                {
                    //no
                    ans = jm.Singlevalue("no");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }
            return ans;
        }

        public string PUpdateProfile(string id, string name, string gender, string age, string address, string city, string state, string cont, string email)
        {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select * from Register where Email='" + email + "' AND id<>'" + id + "'", con);
                DataSet ds = new DataSet();
                da.Fill(ds);

                if (ds.Tables[0].Rows.Count > 0)
                {
                    //already
                    ans = jm.Singlevalue("already");
                }
                else
                {
                    cmd = new SqlCommand("update Register set Name='" + name + "',Gender='" + gender + "',Age='" + age + "',Address='" + address +
                        "',Mobile='" + cont + "',Email='" + email + "' where id='" + id + "'", con);
                    con.Open();
                    cmd.ExecuteNonQuery();
                    con.Close();

                    //true
                    ans = jm.Singlevalue("true");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }

            return ans;
        }

        public string PChangePass(string id, string oldpass, string newpass)
        {
            string ans = "";

            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select Pass from Register where id='" + id + "' AND Password='" + oldpass + "'", con);
                DataSet ds = new DataSet();
                da.Fill(ds);

                if (ds.Tables[0].Rows.Count > 0)
                {
                    cmd = new SqlCommand("update Register set Password='" + newpass + "' where id='" + id + "'", con);
                    con.Open();
                    cmd.ExecuteNonQuery();
                    con.Close();

                    //true
                    ans = jm.Singlevalue("true");
                }
                else
                {
                    //false
                    ans = jm.Singlevalue("false");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }

            return ans;
        }

        public string AnalyseHeart(string uid, string chestpain, string bloodsugar, string restecg, string exang,
            string ca, string slope, string thal, string bloodpressure, string cholesterol, string thalach, string oldpeak)
        {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select Top 1 Dname from TrainingData order by DName", con);
                DataSet ds = new DataSet();
                da.Fill(ds);
                if (ds.Tables[0].Rows.Count > 0)
                {
                    //Dname
                    ans = jm.Maker(ds);
                }
                else
                {
                    //no
                    ans = jm.Singlevalue("no");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }
            return ans;
        }

        //getDoctors()

        //SRC - NAME/SPECIAL
        public string searchDoctor(string src, string query) {
            string ans = "";
            try
            {
                SqlDataAdapter da;
                if (src == "Name") {
                     da = new SqlDataAdapter("SELECT * from DocDetails Where Name LIKE '%" + query + "%' order by Name", con);
                }
                else {
                    da = new SqlDataAdapter("SELECT * from DocDetails Where Specialist LIKE '%" + query + "%' order by Name", con);
                }
                
                DataSet ds = new DataSet();
                da.Fill(ds);
                if (ds.Tables[0].Rows.Count > 0)
                {
                    //did,name,add,mobile,email, age, gender, category
                    ans = jm.Maker(ds);
                }
                else
                {
                    //no
                    ans = jm.Singlevalue("no");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }
            return ans;
        }

        public string AddFeedBack(string uid, string feedback, string date, string time)
        {
            string ans = "";
            try
            {
                cmd = new SqlCommand("insert into Feedback values('"+ uid + "','" + feedback + "','" + date + "','" + time + "')", con);
                con.Open();
                cmd.ExecuteNonQuery();
                con.Close();

                ans = jm.Singlevalue("true");
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }

            return ans;
        }

        public string PgetFeedback(string uid)
        {
            string ans = "";
            try
            {
                SqlDataAdapter da = new SqlDataAdapter("select (select Name from Register where id = f.Uid),f.Feedback,f.Date,f.Time from Feedback f Where f.Uid = '" 
                    + uid + "' ORDER BY f.Uid DESC", con);
                DataSet ds = new DataSet();
                da.Fill(ds);
                if (ds.Tables[0].Rows.Count > 0)
                {
                    //feed,date,time
                    ans = jm.Maker(ds);
                }
                else
                {
                    //no
                    ans = jm.Singlevalue("no");
                }
            }
            catch (Exception e)
            {
                ans = jm.Error(e.Message);
            }
            return ans;
        }

    }
}
