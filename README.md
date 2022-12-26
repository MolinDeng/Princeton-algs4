# Princeton-algs4
This repository contains my solution for the coursera course Algorithm I &amp; II

## Tips

### [Hello, World](https://coursera.cs.princeton.edu/algs4/assignments/hello/specification.php)

Knuth’s method: when reading the ith word, select it with probability 1/i to be the champion, replacing the previous champion. After reading all of the words, print the surviving champion.

### [Percolation](https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php)

How to deal with *backwash*: ![](./misc/008vxvgGgy1h9gzvmh54bj30a4064dfy.jpg)

* Cause: We use two virtual sites to denote `top` and `bottom` respectively. When `top` and `bottom` is connected, those open sites that connected with `bottom` will also connect with `top`, just like left corner of the image.
* Solution: Use two union-find. One has two virtual sites, `top` and `bottom`, for *percolation* evaluation. Another has only one virtual site, `top`, for *full* evaluation.
