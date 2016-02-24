# MIPS

# Example of creating bam list
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
  perl -lane 'next if $_ =~ /.bai$/; $_ =~ /[_]([A-Z]+\d+)[.]dedup/; print "$1\t$_"' \
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
