import javax.swing.JOptionPane;

enum Color { Red, Black }    //the enum values for the colors of the tree objects


class Book{   //The Book Class
    
	private int isbn;
	private int floorNum;
	private int shelfNum;
	
	public Book(int isbn, int floorNum, int shelfNum){   //Book class constructor
		this.isbn = isbn;  //ISBN number
		this.floorNum = floorNum;  //Floor Number
		this.shelfNum = shelfNum;  //Shelf Number
	}
	
	public int getIsbn(){  //get the book's ISBN
		return isbn;
	}
	
	public int getFn(){ //get floor number
		return floorNum;
	}
	
	public int getSn(){  //get shelf number
		return shelfNum;
	}
}


class TreeObject<K extends Comparable<? super K>,V>				//Object class(i.e. nodes) to hold book objects
{																//for the RBT
    public K key;
    public V value;
    public TreeObject<K,V> left;
    public TreeObject<K,V> right;
    public TreeObject<K,V> parent;
    public Color color;

  //Object constructor
    public TreeObject(K key, V value, Color objectColor, TreeObject<K,V> left, TreeObject<K,V> right) {
        this.key = key;
        this.value = value;
        this.color = objectColor;					
        this.left = left;
        this.right = right;
        if (left  != null) { 
        	left.parent = this;
        }
        if (right != null) {
        	right.parent = this;
        }
        this.parent = null;
    }
    public TreeObject<K,V> grandpa() {
        assert parent != null; // it is not the root
        assert parent.parent != null; // it is not a child of root
        return parent.parent;
    }
    
    public TreeObject<K,V> UNKLE() { //see the music group :P
        assert parent != null; // The root has no uncle 
        assert parent.parent != null; // The root's children have no uncle
        return parent.gardas();
    }
    
    public TreeObject<K,V> gardas() { //see first answer http://answers.yahoo.com/question/index?qid=20090905143942AA5UtLT
        assert parent != null; // the root is  all alone :(
        if (this == parent.left)
            return parent.right;
        else
            return parent.left;
    }
    
}


class RBT<K extends Comparable<? super K>,V>		//Red-Black Tree
{
    

    public TreeObject<K,V> root;
    public boolean checkRBT = true;

    public RBT() {			//RBT constructor
        root = null;
        checkProp();   //to check the properties mentioned in the course(taken from my notebook)
    }
    
    public void insert(K key, V value) {
    	TreeObject<K,V> insertedObject = new TreeObject<K,V>(key, value, Color.Red, null, null);
        if (root == null) {
            root = insertedObject;
        } else {
        	TreeObject<K,V> o = root;
            while (true) {
                int compResult = key.compareTo(o.key);
                if (compResult == 0) {
                    o.value = value;
                    return;
                } else if (compResult < 0) {
                    if (o.left == null) {
                        o.left = insertedObject;
                        break;
                    } else {
                        o = o.left;
                    }
                } else {
                    assert compResult > 0;
                    if (o.right == null) {
                        o.right = insertedObject;
                        break;
                    } else {
                        o = o.right;
                    }
                }
            }
            insertedObject.parent = o;
        }
        insertCase1(insertedObject);
        checkProp();
    }
    
    
    private void insertCase1(TreeObject<K,V> o) { //the new object will be at the root of the tree 
        if (o.parent == null)						//and will be black
            o.color = Color.Black;
        else
            insertCase2(o);
    }
    private void insertCase2(TreeObject<K,V> o) { //the object's parent will be black
        if (objectColor(o.parent) == Color.Black)
            return; // Tree is still valid
        else
            insertCase3(o);
    }
    void insertCase3(TreeObject<K,V> o) {    
        if (objectColor(o.UNKLE()) == Color.Red) {		//if the object's uncle is red, the parent and the uncle
            o.parent.color = Color.Black;				//will be repainted black whereas the grandparent will be painted 
            o.UNKLE().color = Color.Black;				//red, so the cases will be checked from the beginning to find any
            o.grandpa().color = Color.Red;			    //violations
            insertCase1(o.grandpa());
        } else {
            insertCase4(o);
        }
    }
    void insertCase4(TreeObject<K,V> o) {
        if (o == o.parent.right && o.parent == o.grandpa().left) {   
            leftRotation(o.parent);											//necessary rotations to correct the form
            o = o.left;														//of the tree if needed, and then their 
        } else if (o == o.parent.left && o.parent == o.grandpa().right) {   // properties will be fixed in case 5
            rightRotation(o.parent);
            o = o.right;
        }
        insertCase5(o);
    }
    void insertCase5(TreeObject<K,V> o) {
        o.parent.color = Color.Black;									//the final case to fix the form and properties
        o.grandpa().color = Color.Red;									//of the tree
        if (o == o.parent.left && o.parent == o.grandpa().left) {
            rightRotation(o.grandpa());
        } else {
            assert o == o.parent.right && o.parent == o.grandpa().right;
            leftRotation(o.grandpa());
        }
    }
    
    private void leftRotation(TreeObject<K,V> o) {
    	TreeObject<K,V> r = o.right;
        replaceObject(o, r);
        o.right = r.left;
        if (r.left != null) {
            r.left.parent = o;
        }
        r.left = o;
        o.parent = r;
    }

    private void rightRotation(TreeObject<K,V> o) {
    	TreeObject<K,V> l = o.left;
        replaceObject(o, l);
        o.left = l.right;
        if (l.right != null) {
            l.right.parent = o;
        }
        l.right = o;
        o.parent = l;
    }
    
    public void checkProp() {
        if (checkRBT) {
        	checkProp1(root);  //an object is either red or black
        	checkProp2(root);  //the root is always black
        	checkProp3(root);  //a red object has black parent
        	checkProp4(root);  //Every path from an object to descendent leaf contains the same number of blacks
        }
    }
    private static void checkProp1(TreeObject<?,?> o) {
        assert objectColor(o) == Color.Red || objectColor(o) == Color.Black;
        if (o == null) return;
        checkProp1(o.left);
        checkProp1(o.right);
    }
    private static void checkProp2(TreeObject<?,?> root) {
        assert objectColor(root) == Color.Black;
    }
    private static Color objectColor(TreeObject<?,?> o) {
        return o == null ? Color.Black : o.color;
    }
    private static void checkProp3(TreeObject<?,?> o) {
        if (objectColor(o) == Color.Red) {
            assert objectColor(o.left)   == Color.Black;
            assert objectColor(o.right)  == Color.Black;
            assert objectColor(o.parent) == Color.Black;
        }
        if (o == null) return;
        checkProp3(o.right);
        checkProp3(o.left);
    }
    private static void checkProp4(TreeObject<?,?> root) {
    	checkProp4Helper(root, 0, -1);
    }

    private static int checkProp4Helper(TreeObject<?,?> o, int countBlack, int totalBlack) {
        if (objectColor(o) == Color.Black) {
        	countBlack++;
        }
        if (o == null) {
            if (totalBlack == -1) {
            	totalBlack = countBlack;
            } else {
                assert countBlack == totalBlack;
            }
            return totalBlack;
        }
        totalBlack = checkProp4Helper(o.left,  countBlack, totalBlack);
        totalBlack = checkProp4Helper(o.right, countBlack, totalBlack);
        return totalBlack;
    }
    
    public V search(K key) {
    	TreeObject<K,V> o = searchObject(key);
        return o == null ? null : o.value;
    }
    
    private TreeObject<K,V> searchObject(K key) {
    	TreeObject<K,V> o = root;
        while (o != null) {
            int compResult = key.compareTo(o.key);
            if (compResult == 0) {
            	return o;
            } else if (compResult < 0) {
                o = o.left;
            } else {
                assert compResult > 0;
                o = o.right;
            }
        }
        return o;
        
    }

    private void replaceObject(TreeObject<K,V> old_obj, TreeObject<K,V> new_obj) {
        if (old_obj.parent == null) {
            root = new_obj;
        } else {
            if (old_obj == old_obj.parent.left)
                old_obj.parent.left = new_obj;
            else
                old_obj.parent.right = new_obj;
        }
        if (new_obj != null) {
            new_obj.parent = old_obj.parent;
        }
    }
   
    
    public static void main(String[] args) {
    	
    	String cont = null;
    	RBT<Integer,Book> lib = new RBT<Integer,Book>();  //RBT instantiation
          
    	//insertion of the book objects into the RBT
          lib.insert(1001, new Book(1001, 2, 23));
          lib.insert(1002, new Book(1002, 3, 34));
          lib.insert(1003, new Book(1003, 1, 13));
          lib.insert(1004, new Book(1004, 4, 23));
          lib.insert(1005, new Book(1005, 2, 26));
          lib.insert(1006, new Book(1006, 1, 67));
          lib.insert(2001, new Book(2001, 3, 64));
          lib.insert(2002, new Book(2002, 4, 89));
          lib.insert(2003, new Book(2003, 2, 98));
          lib.insert(2004, new Book(2004, 1, 68));
          lib.insert(2005, new Book(2005, 1, 90));
          lib.insert(3001, new Book(3001, 3, 3));
          lib.insert(3002, new Book(3002, 1, 21));
          
         
          do{
        	  int skey = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the book ISBN you'd like to search"));
              Book sb = (Book)lib.search(skey);
          
            if(sb!=null){
        	  
        	  JOptionPane.showMessageDialog(null, "The book you have searched for is on the " + sb.getFn() + 
         			 ". floor at shelf " + sb.getSn());
             }
            else {
            	
        	  JOptionPane.showMessageDialog(null, "No such book");
        	 
             }
            cont = JOptionPane.showInputDialog(null, "Would you like to continue(y/n)?");              ;
         
          }while(!(cont.equals("n")));     
  }
}

