using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data;
using System.ComponentModel;
using Accord.Math;
using Accord.MachineLearning;
using Accord.Compat;
using System.Runtime.Serialization;
using System.Reflection;
using Accord.Statistics.Filters;
using Accord;

/// <summary>
/// Summary description for Codification
/// </summary>
public enum CodificationVariable
{
    Default = Ordinal,
    Ordinal = 0,
    Categorical = 1,
    CategoricalWithBaseline = 2,
    Continuous = 3,
    Discrete = 4
}
[Serializable]
#if NETSTANDARD2_0
    [SurrogateSelector(typeof(Codification.Selector))]
#endif
public class Codification : Codification<string>, IAutoConfigurableFilter, ITransform<string[], double[]>
{
    // TODO: Mark redundant methods as obsolete

    /// <summary>
    ///   Creates a new Codification Filter.
    /// </summary>
    /// 
    public Codification()
    {
    }

#if !NETSTANDARD1_4
    /// <summary>
    ///   Creates a new Codification Filter.
    /// </summary>
    /// 
    public Codification(DataTable data)
        : base(data)
    {
    }

    /// <summary>
    ///   Creates a new Codification Filter.
    /// </summary>
    /// 
    public Codification(DataTable data, params string[] columns)
        : base(data, columns)
    {
    }
#endif

    /// <summary>
    ///   Creates a new Codification Filter.
    /// </summary>
    /// 
    public Codification(string columnName, params string[] values)
        : base(columnName, values)
    {
    }

    /// <summary>
    ///   Creates a new Codification Filter.
    /// </summary>
    /// 
    public Codification(string[] columnNames, string[][] values)
        : base(columnNames, values)
    {
    }

    /// <summary>
    ///   Creates a new Codification Filter.
    /// </summary>
    /// 
    public Codification(string columnName, string[][] values)
        : base(columnName, values)
    {
    }

    /// <summary>
    ///   Transforms a matrix of key-value pairs (where the first column
    ///   denotes a key, and the second column a value) into their integer
    ///   vector representation.
    /// </summary>
    /// <param name="values">A 2D matrix with two columns, where the first column contains
    ///   the keys (i.e. "Date") and the second column the values (i.e. "14/05/1988").</param>
    ///   
    /// <returns>A vector of integers where each element contains the translation
    ///   of each respective row in the given <paramref name="values"/> matrix.</returns>
    /// 
    public int[] Transform(string[,] values)
    {
        int rows = values.Rows();
        int cols = values.Columns();
        if (cols != 2)
            throw new DimensionMismatchException("values", "The matrix should contain two columns. The first column should contain the key, and the second should contain the value.");

        int[] result = new int[rows];
        for (int i = 0; i < rows; i++)
            result[i] = Columns[values[i, 0]].Transform(values[i, 1]);
        return result;
    }

    /// <summary>
    ///   Translates a value of a given variable
    ///   into its integer (codeword) representation.
    /// </summary>
    /// 
    /// <param name="columnName">The name of the variable's data column.</param>
    /// <param name="value">The value to be translated.</param>
    /// 
    /// <returns>An integer which uniquely identifies the given value
    /// for the given variable.</returns>
    /// 
    [Obsolete("Please use Transform(columnName, value) instead.")]
    public int Translate(string columnName, string value)
    {
        return Transform(columnName, value);
    }

    /// <summary>
    ///   Translates an array of values into their
    ///   integer representation, assuming values
    ///   are given in original order of columns.
    /// </summary>
    /// 
    /// <param name="data">The values to be translated.</param>
    /// 
    /// <returns>An array of integers in which each value
    /// uniquely identifies the given value for each of
    /// the variables.</returns>
    /// 
    [Obsolete("Please use Transform(data) instead.")]
    public int[] Translate(params string[] data)
    {
        return Transform(data);
    }

#if !NETSTANDARD1_4
    /// <summary>
    ///   Translates an array of values into their
    ///   integer representation, assuming values
    ///   are given in original order of columns.
    /// </summary>
    /// 
    /// <param name="row">A <see cref="DataRow"/> containing the values to be translated.</param>
    /// <param name="columnNames">The columns of the <paramref name="row"/> containing the
    /// values to be translated.</param>
    /// 
    /// <returns>An array of integers in which each value
    /// uniquely identifies the given value for each of
    /// the variables.</returns>
    /// 
    [Obsolete("Please use Transform(row, columnNames) instead.")]
    public int[] Translate(DataRow row, params string[] columnNames)
    {
        return Transform(row, columnNames);
    }
#endif

    /// <summary>
    ///   Translates a value of the given variables
    ///   into their integer (codeword) representation.
    /// </summary>
    /// 
    /// <param name="columnNames">The names of the variable's data column.</param>
    /// <param name="values">The values to be translated.</param>
    /// 
    /// <returns>An array of integers in which each integer
    /// uniquely identifies the given value for the given 
    /// variables.</returns>
    /// 
    [Obsolete("Please use Transform(columnNames, values) instead.")]
    public int[] Translate(string[] columnNames, string[] values)
    {
        return Transform(columnNames, values);
    }

    /// <summary>
    ///   Translates a value of the given variables
    ///   into their integer (codeword) representation.
    /// </summary>
    /// 
    /// <param name="columnName">The variable name.</param>
    /// <param name="values">The values to be translated.</param>
    /// 
    /// <returns>An array of integers in which each integer
    /// uniquely identifies the given value for the given 
    /// variables.</returns>
    /// 
    [Obsolete("Please use Transform(columnName, value) instead.")]
    public int[] Translate(string columnName, string[] values)
    {
        return Transform(columnName, values);
    }

    /// <summary>
    ///   Translates a value of the given variables
    ///   into their integer (codeword) representation.
    /// </summary>
    /// 
    /// <param name="columnName">The variable name.</param>
    /// <param name="values">The values to be translated.</param>
    /// 
    /// <returns>An array of integers in which each integer
    /// uniquely identifies the given value for the given 
    /// variables.</returns>
    /// 
    [Obsolete("Please use Transform(columnName, values) instead.")]
    public int[][] Translate(string columnName, string[][] values)
    {
        return Transform(columnName, values);
    }

    /// <summary>
    ///   Translates an integer (codeword) representation of
    ///   the value of a given variable into its original
    ///   value.
    /// </summary>
    /// 
    /// <param name="columnName">The variable name.</param>
    /// <param name="codeword">The codeword to be translated.</param>
    /// 
    /// <returns>The original meaning of the given codeword.</returns>
    /// 
    [Obsolete("Please use Revert(columnName, codeword) instead.")]
    public string Translate(string columnName, int codeword)
    {
        return Revert(columnName, codeword);
    }

    /// <summary>
    ///   Translates an integer (codeword) representation of
    ///   the value of a given variable into its original
    ///   value.
    /// </summary>
    /// 
    /// <param name="columnName">The name of the variable's data column.</param>
    /// <param name="codewords">The codewords to be translated.</param>
    /// 
    /// <returns>The original meaning of the given codeword.</returns>
    /// 
    [Obsolete("Please use Revert(columnName, codewords) instead.")]
    public string[] Translate(string columnName, int[] codewords)
    {
        return Revert(columnName, codewords);
    }

    /// <summary>
    ///   Translates the integer (codeword) representations of
    ///   the values of the given variables into their original
    ///   values.
    /// </summary>
    /// 
    /// <param name="columnNames">The name of the variables' columns.</param>
    /// <param name="codewords">The codewords to be translated.</param>
    /// 
    /// <returns>The original meaning of the given codewords.</returns>
    /// 
    [Obsolete("Please use Revert(columnNames, codewords) instead.")]
    public string[] Translate(string[] columnNames, int[] codewords)
    {
        return Revert(columnNames, codewords);
    }

#if !NETSTANDARD1_4
    /// <summary>
    ///   Auto detects the filter options by analyzing a given <see cref="System.Data.DataTable"/>.
    /// </summary> 
    ///  
    public void Detect(DataTable data, string[] columns)
    {
        for (int i = 0; i < columns.Length; i++)
            this.Add(new Options(columns[i]).Learn(data));
    }

    /// <summary>
    ///   Auto detects the filter options by analyzing a given <see cref="System.Data.DataTable"/>.
    /// </summary> 
    ///  
    public void Detect(DataTable data)
    {
        Learn(data);
    }
#endif

    /// <summary>
    ///   Auto detects the filter options by analyzing a set of string labels.
    /// </summary>
    /// 
    /// <param name="columnName">The variable name.</param>
    /// <param name="values">A set of values that this variable can assume.</param>
    /// 
    public void Detect(string columnName, string[] values)
    {
        this.Add(new Options(columnName).Learn(values));
    }

    /// <summary>
    ///   Auto detects the filter options by analyzing a set of string labels.
    /// </summary>
    /// 
    /// <param name="columnName">The variable name.</param>
    /// <param name="values">A set of values that this variable can assume.</param>
    /// 
    public void Detect(string columnName, string[][] values)
    {
        Detect(columnName, values.Reshape(0));
    }

    /// <summary>
    ///   Auto detects the filter options by analyzing a set of string labels.
    /// </summary>
    /// 
    /// <param name="columnNames">The variable names.</param>
    /// <param name="values">A set of values that those variable can assume.
    ///   The first element of the array is assumed to be related to the first
    ///   <paramref name="columnNames">column name</paramref> parameter.</param>
    /// 
    public void Detect(string[] columnNames, string[][] values)
    {
        for (int i = 0; i < columnNames.Length; i++)
            this.Add(new Options(columnNames[i]).Learn(values.GetColumn(i)));
    }




    #region Serialization backwards compatibility
#if NETSTANDARD2_0
        internal class Selector : SurrogateSelector
        {
            sealed class DBNullSerializationSurrogate : ISerializationSurrogate
            {
                const string representation = "__System.DBNull__";

                public void GetObjectData(Object obj, SerializationInfo info, StreamingContext context)
                {
                    if (obj as System.DBNull != null)
                        info.AddValue("value", representation);
                }

                public Object SetObjectData(Object obj, SerializationInfo info, StreamingContext context, ISurrogateSelector selector)
                {
                    if (info.GetString("value") == representation)
                        return System.DBNull.Value;
                    return null;
                }
            }

            public Selector()
            {
                AddSurrogate(typeof(System.DBNull), new StreamingContext(StreamingContextStates.All), new DBNullSerializationSurrogate());
            }
        }
#endif
    #endregion

}