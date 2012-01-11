package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetree.example.treemodel;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;

public class TreeModelTest
{

    public static void main (String[] args)
    {
	List<Point> points = new ArrayList<Point>(); 
	points.add( new Point(12,13) ); 
	points.add( new Point(2,123) ); 
	points.add( new Point(23,13) ); 
	@SuppressWarnings("unused")
	JTree tree = new JTree( new PointModel(points) );
    }
}
