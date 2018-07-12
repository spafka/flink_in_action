#!/usr/bin/env bash


# gg update
mkdir gccupdate
cd gccupdate
yum install wget -y

wget http://mirrors-usa.go-parts.com/gcc/releases/gcc-4.8.2/gcc-4.8.2.tar.gz

tar -zxvf gcc-4.8.2.tar.gz

cd gcc-4.8.2
./contrib/download_prerequisites

mkdir gcc-build-4.8.2
cd gcc-build-4.8.2

../configure -enable-checking=release -enable-languages=c,c++ -disable-multilib

make
make install
