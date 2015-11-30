using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace SearchAndRemove
{
    class Program
    {
        static void Main(string[] args)
        {
            // https://msdn.microsoft.com/en-us/library/ezwyzy7b.aspx
            // https://msdn.microsoft.com/en-us/library/8bh11f1k.aspx

            while (true)
            {
                // find file
                bool fileExists = false;
                string path = "";
                while (!fileExists)
                {
                    Console.Write("Write path to .json file: ");
                    path = Console.ReadLine();

                    if (File.Exists(path))
                        fileExists = true;
                }


                // open file
                string text = System.IO.File.ReadAllText(path);

                // remove strings
                text = RemoveBetween(text, '\"', '{');

                // add curly braces
                string withBraces = text.Replace("\"appid\"", "{\"appid\"");

                System.IO.File.WriteAllText("trimmed_" + path, withBraces);

                // save new file
                Console.WriteLine(path + " has been trimmed and saved as " + "trimmed_" + path);
            }
        }

        static string RemoveBetween(string s, char begin, char end)
        {
            // https://stackoverflow.com/questions/1359412/c-sharp-remove-text-in-between-delimiters-in-a-string-regex
            Regex regex = new Regex(string.Format("\\{0}.*?\\{1}", begin, end));
            //return regex.Replace(s, string.Empty);

            return new Regex(" +").Replace(regex.Replace(s, string.Empty), " ");
        }
    }
}
