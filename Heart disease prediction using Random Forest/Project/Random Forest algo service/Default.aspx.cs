using Accord.Math;
using JSONWebAPI;
using System;
using System.Activities.Statements;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class _Default : System.Web.UI.Page
{

    private static SqlConnection con;
    string age, gender, chestpain, bloodsugar, restecg, exang, slope, ca, thal, bloodpressure, cholestrol, thalach, oldpeak;
    string[] allstring;
    protected void Page_Load(object sender, EventArgs e)
    {
        con = new SqlConnection(@"Data Source=43.255.152.25;Initial Catalog=AHeartRandom;Persist Security Info=True;User ID=AHeartRandom;Password=ls*30W9z");
        allstring = null;

        if (!IsPostBack)
        {
            age = Request.QueryString["age"];
            gender = Request.QueryString["gender"];
            chestpain = Request.QueryString["chestpain"];
            bloodsugar = Request.QueryString["bloodsugar"];
            restecg = Request.QueryString["restecg"];
            exang = Request.QueryString["exang"];
            slope = Request.QueryString["slope"];
            ca = Request.QueryString["ca"];
            thal = Request.QueryString["thal"];
            bloodpressure = Request.QueryString["bloodpressure"];
            cholestrol = Request.QueryString["cholestrol"];
            thalach = Request.QueryString["thalach"];
            oldpeak = Request.QueryString["oldpeak"];

            allstring = new string[] { age, gender, chestpain, bloodsugar, restecg, exang, slope, ca, thal, bloodpressure, cholestrol, thalach, oldpeak };

            DataTable table = new DataTable();
            table.Columns.Add("Dname", typeof(string));
            table.Columns.Add("Age", typeof(string));
            table.Columns.Add("Gender", typeof(string));
            table.Columns.Add("ChestPain", typeof(string));
            table.Columns.Add("BloodSugar", typeof(string));
            table.Columns.Add("Restecg", typeof(string));
            table.Columns.Add("Exang", typeof(string));
            table.Columns.Add("Slope", typeof(string));
            table.Columns.Add("CA", typeof(string));
            table.Columns.Add("Thal", typeof(string));
            table.Columns.Add("BloodPressure", typeof(string));
            table.Columns.Add("Cholesterol", typeof(string));
            table.Columns.Add("Thalach", typeof(string));
            table.Columns.Add("Oldpeak", typeof(string));

            string str = "select * from TrainingData";
            SqlDataAdapter da = new SqlDataAdapter(str, con);
            DataSet ds = new DataSet();
            da.Fill(ds);

            for (int i = 0; i < ds.Tables[0].Rows.Count; i++)
            {
                table.Rows.Add(
                    ds.Tables[0].Rows[i][1].ToString(),
                    ds.Tables[0].Rows[i][2].ToString(),
                    ds.Tables[0].Rows[i][3].ToString(),
                    ds.Tables[0].Rows[i][4].ToString(),
                    ds.Tables[0].Rows[i][5].ToString(),
                    ds.Tables[0].Rows[i][6].ToString(),
                    ds.Tables[0].Rows[i][7].ToString(),
                    ds.Tables[0].Rows[i][8].ToString(),
                    ds.Tables[0].Rows[i][9].ToString(),
                    ds.Tables[0].Rows[i][10].ToString(),
                    ds.Tables[0].Rows[i][11].ToString(),
                    ds.Tables[0].Rows[i][12].ToString(),
                    ds.Tables[0].Rows[i][13].ToString(),
                    ds.Tables[0].Rows[i][14].ToString());
            }

            Accord.Math.Random.Generator.Seed = 1;
            var codebook = new Codification()
            {
                DefaultMissingValueReplacement = Double.NaN
            };
            codebook.Learn(table);
            DataTable symbols = codebook.Apply(table);
            string[] inputNames = new[] { "Age", "Gender", "ChestPain", "BloodSugar", "Restecg", "Exang", "Slope", "CA", "Thal", "BloodPressure", "Cholesterol", "Thalach", "Oldpeak" };
            double[][] inputs = symbols.ToJagged(inputNames);
            int[] outputs = symbols.ToArray<int>("Dname");
            var teacher = new RandomForestLearning(table)
            {
                NumberOfTrees = 1,
                SampleRatio = 1.0
            };
            var forest = teacher.Learn(inputs, outputs);
            int[] predicted = forest.Decide(inputs);
            string final = "";
            foreach (int s in predicted)
            {
                final += s.ToString() + ", ";
            }

            int[] query = codebook.Transform(new[,]
            {
            { "Age",age},
            { "Gender", gender },
            { "ChestPain",chestpain },
            { "BloodSugar", bloodsugar },
            { "Restecg", restecg },
            { "Exang",exang},
            { "Slope",slope},
            { "CA", ca },
            { "Thal",thal },
            { "BloodPressure", bloodpressure},
            { "Cholesterol",cholestrol },
            { "Thalach", thalach},
            { "Oldpeak", oldpeak}
        });

            int predicted1 = forest.Decide(query);
            string answer = codebook.Revert("Dname", predicted1);

            JSONMaker jm = new JSONMaker();
            if (answer != null && answer.Length > 0)
            {
                answer = jm.Singlevalue(answer);
            }
            else
            {
                answer = jm.Singlevalue("no");
            }

            Response.Clear();
            Response.ContentType = "application/json; charset=utf-8";
            Response.Write(answer);
            Response.End();
        
    }
            
    }

}