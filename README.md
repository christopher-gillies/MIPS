# MIPS

# Example of creating bam list
```
#Directory listing
ls /Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/* 
/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_A1.dedup.sort.bam
/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_A1.dedup.sort.bam.bai
/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_A2.dedup.sort.bam
/Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/160202sampson_mip1_A2.dedup.sort.bam.bai

#Code to create bam list
ls /Users/cgillies/Documents/MIPS/run_2_24_2016/dedup/* | \
  perl -lane 'next if $_ =~ /.bai$/; $_ =~ /[_]([A-Z]+\d+)[.]dedup/; print "$1\t$_"' \
  > /Users/cgillies/Documents/MIPS/run_2_24_2016/bam.list.txt
```
