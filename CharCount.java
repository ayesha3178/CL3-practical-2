import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CharCount
{

    // Mapper Class
    public static class MyMapper
    extends Mapper<Object, Text, Text, IntWritable>
    {

        public void map(Object key, Text value, Context context)
        throws IOException, InterruptedException
        {

            String line = value.toString();

            for(char ch : line.toCharArray())
            {
                context.write(
                new Text(String.valueOf(ch)),
                new IntWritable(1));
            }
        }
    }

    // Reducer Class
    public static class MyReducer
    extends Reducer<Text, IntWritable, Text, IntWritable>
    {

        public void reduce(Text key,
                           Iterable<IntWritable> values,
                           Context context)
        throws IOException, InterruptedException
        {

            int sum = 0;

            for(IntWritable val : values)
            {
                sum += val.get();
            }

            context.write(key, new IntWritable(sum));
        }
    }

    // Main Method
    public static void main(String args[]) throws Exception
    {

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "Character Count");

        job.setJarByClass(CharCount.class);

        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job,
        new Path(args[0]));

        FileOutputFormat.setOutputPath(job,
        new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}


//Open Terminal in VS Code
//Terminal → New Terminal

//For WordCount
//javac -classpath `hadoop classpath` WordCount.java

//For CharCount
//javac -classpath `hadoop classpath` CharCount.java

//Create Jar
//For WordCount
//jar cf wc.jar *.class

//For CharCount
//jar cf cc.jar *.class

//Run Program
//hadoop jar wc.jar WordCount input.txt output1
//hadoop jar cc.jar CharCount input.txt output2

//See Output
//cat output1/part-r-00000
//cat output2/part-r-00000