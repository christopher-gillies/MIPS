# MIPS

# Summarizing coverage
```
MIPS=/Users/cgillies/Documents/workspace-sts-3.6.1.RELEASE/MIPS/release/MIPS-0.0.1.jar
BEDFILE=/Users/cgillies/Documents/MIPS/run_2_24_2016/joined_pairs_picked.gapfill.bed
BAMLIST=/Users/cgillies/Documents/MIPS/run_2_24_2016/bam.list.txt
OUTFILE=/Users/cgillies/Documents/MIPS/run_2_24_2016/summary_of_coverage.txt
java -jar $MIPS --command summarizeCoverage --regionList $BEDFILE --bamList $BAMLIST --outfile $OUTFILE
```

##Investigating results
```
head /Users/cgillies/Documents/MIPS/run_2_24_2016/summary_of_coverage.txt
```

##R script
```
library(ggplot2)
tbl <- read.table("/Users/cgillies/Documents/MIPS/run_2_24_2016/summary_of_coverage.txt",sep="\t",header=T);
rownames(tbl) <- tbl[,"probe"]
#remove probe column
tbl <- tbl[,-1]
#remove last two rows
tbl <- data.frame(tbl[1:(dim(tbl)[1] - 2),]);

meds <- apply(tbl,MARGIN=1,FUN=median)
df <- data.frame(Median=log10(sort(meds,decreasing=TRUE)),Indices=(seq(1,length(meds)))/length(meds));
ggplot(df,aes(x=Indices,y=Median)) + geom_point() + xlab("Cummulative fraction of probes") + ylab("log10 median depth")

```
# Creating a bam list
##Directory listing of bam list
```
ls /Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/* 
/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_A1.dedup.sort.bam
/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_A1.dedup.sort.bam.bai
/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_A2.dedup.sort.bam
/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_A2.dedup.sort.bam.bai
```

##Code to create bam list
* two columns
* sample id
* file path
```
ls /Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/* | \
  perl -lane 'next if $_ =~ /.bai$/; $_ =~ /(\d+)sampson.+[_]([A-Z]+\d+)[.]dedup/; print "$1"."_"."$2\t$_"' \
  > /Users/cgillies/Documents/MIPS/run_2_24_2016/bam.list.txt
```

##Catalog of bam.list.txt
```
head /Users/cgillies/Documents/MIPS/run_2_24_2016/bam.list.txt
A1	/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_A1.dedup.sort.bam
A2	/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_A2.dedup.sort.bam
B1	/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_B1.dedup.sort.bam
C1	/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_C1.dedup.sort.bam
D1	/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_D1.dedup.sort.bam
E1	/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_E1.dedup.sort.bam
F1	/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_F1.dedup.sort.bam
G1	/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_G1.dedup.sort.bam
H1	/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_H1.dedup.sort.bam
A1	/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160203sampson_mip1_A1.dedup.sort.bam
```
