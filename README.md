# Princeton-algs4

This repository contains my solution for the coursera course Algorithm I &amp; II

## Full AC tips

### [Hello, World](https://coursera.cs.princeton.edu/algs4/assignments/hello/specification.php)

Knuth’s method: when reading the ith word, select it with probability 1/i to be the champion, replacing the previous champion. After reading all of the words, print the surviving champion.

### [Percolation](https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php)

How to deal with *backwash*: ![backwash](./misc/008vxvgGgy1h9gzvmh54bj30a4064dfy.jpg)

* Cause: We use two virtual sites to denote `top` and `bottom` respectively. When `top` and `bottom` is connected, those open sites that connected with `bottom` will also connect with `top`, just like left corner of the image.
* Solution: Use two union-find. One has two virtual sites, `top` and `bottom`, for *percolation* evaluation. Another has only one virtual site, `top`, for *full* evaluation.

### [Queues](https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php)

* Permutation Bonus: use only one Deque or RandomizedQueue object of maximum size at most k

>In the ideal world the probability of each string of making it to the output is K/N. Though we do not know N until the whole input stream is read. For large N and small K we certainly do not want to accumulate all N strings before choosing K of them. The method I used in my code is to fill the first K spots in the RandomizedQueue with the first K strings. Then Mth string in the input replaces (via dequeue/enqueue) one of the strings already in the RandomizedQueue with the probability K/M.

```Java
while (!StdIn.isEmpty()) {
    String s = StdIn.readString();
    cnt++;
    if (queue.size() == k) {
        if (StdRandom.bernoulli(1.0 * k / cnt)) {
            queue.dequeue();
            queue.enqueue(s);
        }
    }
    else
        queue.enqueue(s);
}
```

### [Collinear Points](https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php)

* "Return a comparator" reference: <https://stackoverflow.com/questions/6478515/return-type-from-a-comparator>
* How to remove duplicates

> When sorting `points` array in slope order, make sure that it also remains natural order. Then, by only counting segments starts from the smallest point, we can remove duplicates.

lets say `p1, p2, p3, p4, p5` forms a segments in natural order. `p1` is the smallest, `p5` is the largest (natural order)

* When `p1` is slope anchor, `min` is `p1`, `max` is `p5`, add this segment.
* When `p2` is slope anchor, `min` is `p1` not `p2`, `max` is `p5`, discard this duplicates.

### [8-puzzle](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php)<img src="misc/logo.png" width="50"/>

<!-- ### ![](misc/logo.png) [8-puzzle](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php) -->

* `equals` method: Private data is accessible by any instance of that class,
        even if one instance of class A is accessing the private members of another instance of A.
        It's important to remember that that access modifiers (private, protected, public)
        are controlling class access, not instance access.
* Caching the Hamming and Manhattan priorities: not only caching in `Board` class, but also caching in `Node` inner class.
* [A* search algorithm](https://en.wikipedia.org/wiki/A*_search_algorithm)
