/*
 * @(#)StartUp.java - about box of Project-X, with terms of condition and credits
 *
 * Copyright (c) 2004 by dvb.matt, All Rights Reserved. 
 * 
 * This file is part of X, a free Java based demux utility.
 * X is intended for educational purposes only, as a non-commercial test project.
 * It may not be used otherwise. Most parts are only experimental.
 * 
 *
 * This program is free software; you can redistribute it free of charge
 * and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * AboutBox for Project-X GUI.
 * 
 * @author Peter Storch
 */
public class AboutBox extends JDialog
{
	/** Background Color */
	private static final Color BACKGROUND_COLOR = new Color(224,224,224,224);
	
	/**
	 * Constructor of AboutBox.
	 * 
	 * @param frame
	 */
	public AboutBox(Frame frame)
	{
		super(frame, true);
		setTitle(Resource.getString("about.title"));

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.setBorder( BorderFactory.createEmptyBorder(10,10,10,10));
		container.setBackground(BACKGROUND_COLOR);
		
		JLabel logo = new JLabel(Resource.loadIcon("px.gif"));
		logo.setOpaque(true);
		logo.setBackground(BACKGROUND_COLOR);

		String credits[] = Resource.getStringByLines("credits");

		for (int a=0; a<credits.length; a++) 
			container.add(new JLabel(credits[a]));

		container.add(new JLabel(" ")); // as spacer

		String terms[] = Resource.getStringByLines("terms");

		for (int a=0; a<terms.length; a++) 
			container.add(new JLabel(terms[a]));

		JButton ok = new JButton(Resource.getString("about.ok"));
		ok.setBackground(BACKGROUND_COLOR);
		ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) {
				dispose(); 
			}
		});

		JPanel container2 = new JPanel(new BorderLayout());
		//container2.setBorder( BorderFactory.createRaisedBevelBorder());
		container2.setBackground(BACKGROUND_COLOR);
		container2.setOpaque(true);
		container2.add(logo, BorderLayout.NORTH);
		container2.add(container, BorderLayout.CENTER);
		container2.add(ok, BorderLayout.SOUTH);

		getContentPane().add(container2);
		
		pack();
		setLocation(200,200);
		setResizable(false); //DM17042004 081.7 int02 add

		addWindowListener (new WindowAdapter() { 
			public void windowClosing(WindowEvent e) { 
				dispose(); 
			}
		});

		setVisible(true);
	}

}

