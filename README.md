# MIPS

# Summarizing coverage
This analysis could be useful for seeing which probes are not being amplified
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

##R script for coverage
```
library(ggplot2)
tbl <- read.table("/Users/cgillies/Documents/MIPS/run_2_24_2016/summary_of_coverage.txt",sep="\t",header=T);
rownames(tbl) <- tbl[,"probe"]
#remove probe column
tbl <- tbl[,-1]

#total coverage row
total_coverage_per_sample = tbl[dim(tbl)[1] - 1,];

#remove last two rows
tbl <- data.frame(tbl[1:(dim(tbl)[1] - 2),]);
g160202 <- grepl("160202",colnames(tbl))
g160203 <- grepl("160203",colnames(tbl))

pdf("/Users/cgillies/Documents/MIPS/run_2_24_2016/coverage.pdf");
meds_all <- apply(tbl,MARGIN=1,FUN=median)
df_all <- data.frame(Median=log10(sort(meds_all,decreasing=TRUE)),Indices=(seq(1,length(meds_all)))/length(meds_all));
ggplot(df_all,aes(x=Indices,y=Median)) + geom_point() + xlab("Cummulative fraction of probes") + ylab("log10 median read depth per probe") + ggtitle("All samples");


meds_g160202 <- apply(tbl[g160202,],MARGIN=1,FUN=median)
df_g160202 <- data.frame(Median=log10(sort(meds_g160202,decreasing=TRUE)),Indices=(seq(1,length(meds_g160202)))/length(meds_g160202));
ggplot(df_g160202,aes(x=Indices,y=Median)) + geom_point() + xlab("Cummulative fraction of probes") + ylab("log10 median read depth per probe") + ggtitle("160202");

meds_g160203 <- apply(tbl[g160203,],MARGIN=1,FUN=median)
df_g160203 <- data.frame(Median=log10(sort(meds_g160203,decreasing=TRUE)),Indices=(seq(1,length(meds_g160203)))/length(meds_g160203));
ggplot(df_g160203,aes(x=Indices,y=Median)) + geom_point() + xlab("Cummulative fraction of probes") + ylab("log10 median read depth per probe") + ggtitle("160203");

#plot total sample depth in millions
df_total = data.frame(Total=as.numeric(total_coverage_per_sample/10^3))
df_total$Batch = "160202";
df_total$Batch[g160203] = "160203";
df_total$Batch = factor(df_total$Batch);

ggplot(df_total,aes(x=Total,fill=Batch)) + geom_histogram() + xlab("Total reads in thousands");
dev.off();
```

## Coverage of regions (merge overlapping probes and count depth)
This analysis could be useful for seeing which regions are not getting amplified
```
MIPS=/Users/cgillies/Documents/workspace-sts-3.6.1.RELEASE/MIPS/release/MIPS-0.0.1.jar
BEDFILE=/Users/cgillies/Documents/MIPS/run_2_24_2016/joined_pairs_picked.gapfill.bed
BAMLIST=/Users/cgillies/Documents/MIPS/run_2_24_2016/bam.list.txt
OUTFILE=/Users/cgillies/Documents/MIPS/run_2_24_2016/summary_of_coverage_merged_probes.txt
java -jar $MIPS --command summarizeCoverage --regionList $BEDFILE --bamList $BAMLIST --outfile $OUTFILE --mergeOverlappingRegions
```

###Investigating results
```
head /Users/cgillies/Documents/MIPS/run_2_24_2016/summary_of_coverage_merged_probes.txt
```

###R script
```
library(reshape)
library(ggplot2)
tbl <- read.table("/Users/cgillies/Documents/MIPS/run_2_24_2016/summary_of_coverage_merged_probes.txt",sep="\t",header=T);
rownames(tbl) <- tbl[,"probe"]
mtbl <- melt(tbl,c("probe"))
#remove probe column
tbl <- tbl[,-1]
#remove last two rows
tbl <- data.frame(tbl[1:(dim(tbl)[1] - 2),]);
g160202 <- grepl("160202",colnames(tbl))
g160203 <- grepl("160203",colnames(tbl))

pdf("/Users/cgillies/Documents/MIPS/run_2_24_2016/coverage_of_regions.pdf");
meds_all <- apply(tbl,MARGIN=1,FUN=median)
df_all <- data.frame(Median=log10(sort(meds_all,decreasing=TRUE)),Indices=(seq(1,length(meds_all)))/length(meds_all));
ggplot(df_all,aes(x=Indices,y=Median)) + geom_point() + xlab("Cummulative fraction of probes") + ylab("log10 median read depth per region") + ggtitle("All samples");


meds_g160202 <- apply(tbl[g160202,],MARGIN=1,FUN=median)
df_g160202 <- data.frame(Median=log10(sort(meds_g160202,decreasing=TRUE)),Indices=(seq(1,length(meds_g160202)))/length(meds_g160202));
ggplot(df_g160202,aes(x=Indices,y=Median)) + geom_point() + xlab("Cummulative fraction of probes") + ylab("log10 median read depth per region") + ggtitle("160202");

meds_g160203 <- apply(tbl[g160203,],MARGIN=1,FUN=median)
df_g160203 <- data.frame(Median=log10(sort(meds_g160203,decreasing=TRUE)),Indices=(seq(1,length(meds_g160203)))/length(meds_g160203));
ggplot(df_g160203,aes(x=Indices,y=Median)) + geom_point() + xlab("Cummulative fraction of probes") + ylab("log10 median read depth per region") + ggtitle("160203");

dev.off();


#boxplot per sample
mtbl$Batch = "160202";
mtbl$Batch[grepl("160203",mtbl$variable)] = "160203";
mtbl$Batch = factor(mtbl$Batch)
pdf("/Users/cgillies/Documents/MIPS/run_2_24_2016/region_depth_per_sample.pdf",width=20,height=10)
ggplot(mtbl,aes(y=log10(value),x=factor(variable),color=Batch)) + geom_boxplot() + xlab("Sample") + ylab("Log10 depth per region") + ggtitle("Depth for overlapping regions of probes per sample");
dev.off()


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
