/*
 * @(#)VBI.java - carries various stuff 
 *
 * Copyright (c) 2005 by dvb.matt, All Rights Reserved.
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

package net.sourceforge.dvb.projectx.parser;

import net.sourceforge.dvb.projectx.common.Common;
import net.sourceforge.dvb.projectx.common.Resource;
import net.sourceforge.dvb.projectx.common.X;
import net.sourceforge.dvb.projectx.video.Video;

public final class VBI
{
	private static long source_pts = 0;

	private static String vps_str = "";
	private static String vps_sound_mode[] = { "n/a", "mono", "stereo", "dual" };

	private static byte[] wss = new byte[3];

	/**
	 * 
	 */
	private VBI()
	{}


	/**
	 * 
	 */
	public static void reset()
	{
		source_pts = 0;
		vps_str = "";
		wss = new byte[3];
	}

	/**
	 * 
	 */
	// mpg2 pes expected
	public static void parsePES(byte[] packet) throws ArrayIndexOutOfBoundsException
	{
		if (packet[0] != 0 || packet[1] != 0 || packet[2] != 1)
			return;

		int pes_id = 0xFF & packet[3];
		int pes_packetlength = (0xFF & packet[4])<<8 | (0xFF & packet[5]);
		int pes_ext_length = 0xFF & packet[8];
		boolean pts_flag = (0x80 & packet[7]) != 0 ? true : false;

		source_pts = pts_flag ? Video.getPTSfromBytes(packet, 9) : 0;

		decodeVBI(packet, 9 + pes_ext_length);
	}

	/**
	 * 
	 */
	private static void decodeVBI(byte[] packet, int offs) throws ArrayIndexOutOfBoundsException
	{
		String str;

		boolean wss_online = false;

		/**
		 * 0x99..0x9B -> PES contains one or multiple VBI Data definitions
		 */
		if ((0xFC & packet[offs]) != 0x98 || (3 & packet[offs]) == 0)
			return;

		for (int i = offs + 1, data_unit_id, len; i < packet.length - 1; )
		{
			data_unit_id = 0xFF & packet[i++];
			len = 0xFF & packet[i++];

			/**
			 * VPS
			 */
			if (data_unit_id == 0xC3)
			{
				str = decodeVPS(packet, i);

				if (str != null && !str.equals(vps_str))
				{
					vps_str = str;
					X.Msg(Resource.getString("teletext.msg.vps", str) + " " + Common.formatTime_1(source_pts / 90));
				}
			}

			/**
			 * WSS
			 */
			else if (data_unit_id == 0xC4)
			{
				str = decodeWSS(packet, i);

				if (str != null)
				{
					wss_online = true;

					if (str.length() > 0)
					{
						X.Msg("-> WSS Status - changed @ PTS " + Common.formatTime_1(source_pts / 90));
						X.Msg(str);
					}

					else if (wss[2] == 0)
					{
						X.Msg("-> WSS Status - no change @ PTS " + Common.formatTime_1(source_pts / 90));
						wss[2] = -2;
					}
				}
			}

			else if (data_unit_id == 0xFF)
			{}

			i += len;
		}

		if (!wss_online && wss[2] == -1)
		{
			wss[2] = 0;

			X.Msg("-> WSS Status - offline @ PTS " + Common.formatTime_1(source_pts / 90));
		}
	}

	/**
	 * 
	 */
	public static String decodeVPS(byte[] packet, int offs)
	{
		String vps_status = "";

		if (packet.length - 1 < offs + 12)
			return null;

		int vps_data = (0x3F & packet[offs + 9])<<24 |
				(0xFF & packet[offs + 10])<<16 | 
				(0xFF & packet[offs + 11])<<8 | 
				(0xFF & packet[offs + 12]);


		switch (0x1F & vps_data>>>16)
		{ 
			case 0x1C:
				vps_status = "Contin."; 
				break; 

			case 0x1D:
				vps_status = "Pause  "; 
				break; 

			case 0x1E:
				vps_status = "Stop   "; 
				break; 

			case 0x1F:
				vps_status = "Timer  "; 
				break; 

			default: 
				vps_status = "" + formatString(0x1F & (vps_data>>>25)) + "." + 
						formatString(0xF & (vps_data>>>21)) + ".  " +
						formatString(0x1F & (vps_data>>>16)) + ":" + 
						formatString(0x3F & (vps_data>>>10)) + "  ";
		}

		vps_status += vps_sound_mode[(3 & packet[5]>>>6)] + " " + 
				Integer.toHexString(0xF & vps_data>>>6).toUpperCase() + " " + 
				Integer.toHexString(0x3F & vps_data).toUpperCase();

		return vps_status;
	}

	/**
	 * 
	 */
	private static String formatString(int value)
	{
		String str = "00" + String.valueOf(value);

		return str.substring(str.length() - 2);
	}


	/**
	 * 
	 */
	private static String decodeWSS(byte[] packet, int offs)
	{
		if (packet.length - 1 < offs + 2)
			return null;

		if (packet[offs + 1] == wss[0] && packet[offs + 2] == wss[1])
			return "";

		System.arraycopy(packet, offs + 1, wss, 0, 2);
		wss[2] = -1;

		String str = getGroup1(wss);
		str += getGroup2(wss);
		str += getGroup3(wss);
		str += getGroup4(wss);

		return str;
	}

	/**
	 * 
	 */
	private static String getGroup1(byte[] packet)
	{
		String str = "";

		switch (0xF & packet[0]>>4)
		{
		case 1: // 0001  Biphase 01010110
			str = "  " + Resource.getString("wss.group_1.0001");
			break;

		case 8: // 1000  Biphase 10010101
			str = "  " + Resource.getString("wss.group_1.1000");
			break;

		case 4: // 0100  Biphase 01100101
			str = "  " + Resource.getString("wss.group_1.0100");
			break;

		case 0xD: // 1101  Biphase 10100110
			str = "  " + Resource.getString("wss.group_1.1101");
			break;

		case 2: // 0010  Biphase 01011001
			str = "  " + Resource.getString("wss.group_1.0010");
			break;

		case 7: // 0111  Biphase 01101010
			str = "  " + Resource.getString("wss.group_1.0111");
			break;

		case 0xE: // 1110  Biphase 10101001
			str = "  " + Resource.getString("wss.group_1.1110");
			break;

		default:  
			str = "  " + Resource.getString("wss.group_1.error");
		}

		str += "\n";

		return str;
	}

	/**
	 * 
	 */
	private static String getGroup2(byte[] packet)
	{
		String str = "";

		switch (1 & packet[0]>>3)
		{
		case 0: // 0  Biphase 01
			str += "  " + Resource.getString("wss.group_2.0.01");
			break; 

		case 1: // 1  Biphase 10
			str += "  " + Resource.getString("wss.group_2.0.10"); 
		}       

		str += ",";

		switch (1 & packet[0]>>2)
		{
		case 0: // 0  Biphase 01
			str += "  " + Resource.getString("wss.group_2.1.01"); 
			break;

		case 1: // 1  Biphase 10
			str += "  " + Resource.getString("wss.group_2.1.10"); 
		}       

		str += ",";

		switch (1 & packet[0]>>1)
		{
		case 0:  // 0  Biphase 01
			str += "  " + Resource.getString("wss.group_2.2.01"); 
			break;

		case 1:  // 1  Biphase 10
			str += "  " + Resource.getString("wss.group_2.2.10"); 
		}       

		str += ",";

		switch (1 & packet[0])
		{
		case 0: // 0  Biphase 01
			str += "  " + Resource.getString("wss.group_2.3.01"); 
			break;

		case 1:  // 1  Biphase 10
			str += "  " + Resource.getString("wss.group_2.3.10"); 
		}       

		str += "\n";

		return str;
	}

	/**
	 * 
	 */
	private static String getGroup3(byte[] packet)
	{
		String str = "";

		switch (1 & packet[1]>>7)
		{
		case 0: // 0  Biphase 01
			str += "  " + Resource.getString("wss.group_3.0.01"); 
			break;

		case 1: // 1  Biphase 10
			str += "  " + Resource.getString("wss.group_3.0.10"); 
		}       

		str += ",";

		switch (3 & packet[1]>>5)
		{
		case 0:  // 00  Biphase 0101
			str += "  " + Resource.getString("wss.group_3.1.00"); 
			break;

		case 1:  // 01  Biphase 0110
			str += "  " + Resource.getString("wss.group_3.1.01"); 
			break;

		case 2:  // 10  Biphase 1001
			str += "  " + Resource.getString("wss.group_3.1.10"); 
			break; 

		case 3:  // 11  Biphase 1010
			str += "  " + Resource.getString("wss.group_3.1.11"); 
		}       

		str += "\n";

		return str;
	}

	/**
	 * 
	 */
	private static String getGroup4(byte[] packet)
	{
		String str = "";

		switch (1 & packet[1]>>4)
		{
		case 0:  // 0  Biphase 01
			str += "  " + Resource.getString("wss.group_4.0.01");
			break;

		case 1:  // 1  Biphase 10
			str += "  " + Resource.getString("wss.group_4.0.10");
		}       

		str += ",";

		switch (3 & packet[1]>>2)
		{
		case 0: // 00  Biphase 0101
			str += "  " + Resource.getString("wss.group_4.1.00");
			break;

		case 1: // 01  Biphase 0110
			str += "  " + Resource.getString("wss.group_4.1.01"); 
			break; 

		case 2:  // 10  Biphase 1001
			str += "  " + Resource.getString("wss.group_4.1.10"); 
			break;

		case 3:  // 11  Biphase 1010
			str += "  " + Resource.getString("wss.group_4.1.11"); 
		}       

		return str;
	}

}