using System.Configuration;
using System.Data.SqlClient;

namespace JSONWebAPI
{ 
    public class DBConnect
    {

        private static SqlConnection NewCon = new SqlConnection(@"Data Source=43.255.152.25;Initial Catalog=AHeartRandom;Persist Security Info=True;User ID=AHeartRandom;Password=ls*30W9z");

        public static SqlC 
        { 
            return NewCon; 
        }
        public DBConnect()
        {

        }
    }
}