/**
 * 
 */
package net.lahwran.zanminimap;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * @author lahwran
 *
 */
public class Menu {
    public Random generator = new Random();
    /** Current Menu Loaded */
    public int iMenu = 1;


    /** Last click state, to prevent mouse bouncing/repeating */
    public boolean leftbtndown = false, rightbtndown = false, middlebtndown = false;

    /** Menu input string */
    public String inStr = "";

    /** Waypoint name temporary input */
    public String way = "";

    /** Waypoint X coord temp input */
    public int wayX = 0;

    /** Z coord temp input */
    public int wayZ = 0;

    /** Holds error exceptions thrown */
    public String error = "";

    /** Main Menu Option Count */
    public int mmOptCount = 10;

    /** Strings to show for menu */
    public String[][] sMenu = new String[2][15];

    /** Time remaining to show error thrown for */
    public int ztimer = 0;

    /** Key entry interval */
    public int fudge = 0;

    /** Menu level for next render */
    public int next = 0;

    /** Cursor blink interval */
    public int blink = 0;

    /** Last key down on previous render */
    public int lastKey = 0;


    /** Is the scrollbar being dragged? */
    public boolean scrollClick = false;

    /** Scrollbar drag start position */
    public int scrStart = 0;

    /** Scrollbar offset */
    public int sMin = 0;

    /** Scrollbar size */
    public int sMax = 67;

    /** 1st waypoint entry shown */
    public int min = 0;

    private ZanMinimap minimap;
    private Config conf;
    private ObfHub obfhub;
    private MapCalculator mapcalc;

    Menu(ZanMinimap minimap) {

        this.minimap = minimap;
        this.conf = minimap.conf;
        this.obfhub = minimap.obfhub;
        this.mapcalc = minimap.mapcalc;

        for (int m = 0; m < 2; m++)
            for (int n = 0; n < 10; n++)
                this.sMenu[m][n] = "";

        this.sMenu[0][0] = "§4Zan's§F Mod! " + ZanMinimap.zmodver;
        this.sMenu[0][1] = "Welcome to Zan's Minimap, there are a";
        this.sMenu[0][2] = "number of features and commands available to you.";
        this.sMenu[0][3] = "- Press §B" + Keyboard.getKeyName(conf.zoomKey) + " §Fto zoom in/out, or §B" + Keyboard.getKeyName(conf.menuKey) + "§F for options.";
        this.sMenu[1][0] = "Options";
        this.sMenu[1][1] = "Display Coordinates:";
        this.sMenu[1][2] = "Hide Minimap:";
        this.sMenu[1][3] = "Dynamic Lighting:";
        this.sMenu[1][4] = "Terrain Depth:";
        this.sMenu[1][5] = "Square Map:";
        this.sMenu[1][6] = "Welcome Screen:";
        this.sMenu[1][7] = "Threading:";
        this.sMenu[1][8] = "Color:";
        this.sMenu[1][9] = "Netherpoints:";
        this.sMenu[1][10] = "Cavemap:";
    }
    
    public int toInt(String str)
    {
        if(str.startsWith("n"))
        {
            return Integer.parseInt(str.substring(1)) * 8;
        }
        else
        {
            return Integer.parseInt(str);
        }
    }
    
    void showMenu(int scWidth, int scHeight)
    {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (iMenu <= 0) return;
        int height;
        int maxSize = 0;
        int border = 2;
        int MouseX = obfhub.getMouseX(scWidth);
        int MouseY = obfhub.getMouseY(scHeight);
        
        boolean leftclick = false;
        boolean rightclick = false;
        boolean middleclick = false;

        if (Mouse.getEventButtonState()) {
            if (Mouse.getEventButton() == 0) {
                if (!this.leftbtndown)
                    leftclick = true;
                this.leftbtndown = true;
            } else {
                this.leftbtndown = false;
            }
            if (Mouse.getEventButton() == 1) {
                if (!this.rightbtndown)
                    rightclick = true;
                this.rightbtndown = true;
            } else {
                this.rightbtndown = false;
            }
            if (Mouse.getEventButton() == 2) {
                if (!this.middlebtndown)
                    middleclick = true;
                this.middlebtndown = true;
            } else {
                this.middlebtndown = false;
            }
        } else {
            this.leftbtndown = false;
            this.rightbtndown = false;
            this.middlebtndown = false;
        }

        String head = "Waypoints";
        String opt1 = "Exit Menu";
        String opt2 = "Waypoints";
        String opt3 = "Remove";

        if (this.iMenu < 3)
        {
            head = this.sMenu[this.iMenu - 1][0];

            for (height = 1; height <= mmOptCount; height++)
                if (obfhub.chkLen(sMenu[iMenu - 1][height]) > maxSize)
                    maxSize = obfhub.chkLen(sMenu[iMenu - 1][height]);
        }
        else
        {
            opt1 = "Back";

            if (this.iMenu == 4)
                opt2 = "Cancel";
            else
                opt2 = "Add";

            maxSize = 80;

            for (int i = 0; i < conf.wayPts.size(); i++)
                if (obfhub.chkLen((i + 1) + ") " + conf.wayPts.get(i).name) > maxSize)
                    maxSize = obfhub.chkLen((i + 1) + ") " + conf.wayPts.get(i).name) + 32;

            height = 10;
        }

        int title = obfhub.chkLen(head);
        int centerX = (int)((scWidth + 5) / 2.0D);
        int centerY = (int)((scHeight + 5) / 2.0D);
        String hide = "§7Press §F" + Keyboard.getKeyName(conf.zoomKey) + "§7 to hide.";
        int footer = obfhub.chkLen(hide);
        GL11.glDisable(3553);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);
        double leftX = centerX - title / 2.0D - border;
        double rightX = centerX + title / 2.0D + border;
        double topY = centerY - (height - 1) / 2.0D * 10.0D - border - 20.0D;
        double botY = centerY - (height - 1) / 2.0D * 10.0D + border - 10.0D;
        this.drawBox(leftX, rightX, topY, botY);

        if (this.iMenu == 1)
        {
            leftX = centerX - maxSize / 2.0D - border;
            rightX = centerX + maxSize / 2.0D + border;
            topY = centerY - (height - 1) / 2.0D * 10.0D - border;
            botY = centerY + (height - 1) / 2.0D * 10.0D + border;
            this.drawBox(leftX, rightX, topY, botY);
            leftX = centerX - footer / 2.0D - border;
            rightX = centerX + footer / 2.0D + border;
            topY = centerY + (height - 1) / 2.0D * 10.0D - border + 10.0D;
            botY = centerY + (height - 1) / 2.0D * 10.0D + border + 20.0D;
            this.drawBox(leftX, rightX, topY, botY);
        }
        else
        {
            leftX = centerX - maxSize / 2.0D - 25 - border;
            rightX = centerX + maxSize / 2.0D + 25 + border;
            topY = centerY - (height - 1) / 2.0D * 10.0D - border;
            botY = centerY + (height - 1) / 2.0D * 10.0D + border;
            this.drawBox(leftX, rightX, topY, botY);
            this.drawOptions(rightX - border, topY + border, MouseX, MouseY, leftclick, rightclick, middleclick);
            footer = this.drawFooter(centerX, centerY, height, opt1, opt2, opt3, border, MouseX, MouseY, leftclick, rightclick, middleclick);
        }

        GL11.glEnable(3553);
        obfhub.write(head, centerX - title / 2, (centerY - (height - 1) * 10 / 2) - 19, 0xffffff);

        if (this.iMenu == 1)
        {
            for (int n = 1; n < height; n++)
                obfhub.write(this.sMenu[iMenu - 1][n], centerX - maxSize / 2, ((centerY - (height - 1) * 10 / 2) + (n * 10)) - 9, 0xffffff);

            obfhub.write(hide, centerX - footer / 2, ((scHeight + 5) / 2 + (height - 1) * 10 / 2 + 11), 0xffffff);
        }
        else
        {
            if (this.iMenu == 2)
            {
                for (int n = 1; n < height; n++)
                {
                    obfhub.write(this.sMenu[iMenu - 1][n], (int)leftX + border + 1, ((centerY - (height - 1) * 10 / 2) + (n * 10)) - 9, 0xffffff);

                    if (this.chkOptions(n - 1))
                        hide = "On";
                    else
                        hide = "Off";

                    obfhub.write(hide, (int)rightX - border - 15 - obfhub.chkLen(hide) / 2, ((centerY - (height - 1) * 10 / 2) + (n * 10)) - 8, 0xffffff);
                }
            }
            else
            {
                int max = min + 9;

                if (max > conf.wayPts.size())
                {
                    max = conf.wayPts.size();

                    if (min >= 0)
                    {
                        if (max - 9 > 0)
                            min = max - 9;
                        else
                            min = 0;
                    }
                }

                for (int n = min; n < max; n++)
                {
                    int yTop = ((centerY - (height - 1) * 10 / 2) + ((n + 1 - min) * 10));
                    int leftTxt = (int)leftX + border + 1;
                    obfhub.write((n + 1) + ") " + conf.wayPts.get(n).name, leftTxt, yTop - 9, 0xffffff);

                    if (this.iMenu == 4)
                    {
                        hide = "X";
                    }
                    else
                    {
                        if (conf.wayPts.get(n).enabled)
                            hide = "On";
                        else
                            hide = "Off";
                    }

                    obfhub.write(hide, (int)rightX - border - 29 - obfhub.chkLen(hide) / 2, yTop - 8, 0xffffff);

                    if (MouseX > leftTxt && MouseX < (rightX - border - 77) && MouseY > yTop - 10 && MouseY < yTop - 1)
                    {
                        String out = conf.wayPts.get(n).x + ", " + conf.wayPts.get(n).z;
                        int len = obfhub.chkLen(out) / 2;
                        GL11.glDisable(3553);
                        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.8f);
                        this.drawBox(MouseX - len - 1, MouseX + len + 1, MouseY - 11, MouseY - 1);
                        GL11.glEnable(3553);
                        obfhub.write(out, MouseX - len, MouseY - 10, 0xffffff);
                    }
                }
            }

            int footpos = ((scHeight + 5) / 2 + (height - 1) * 10 / 2 + 11);

            if (this.iMenu == 2)
            {
                obfhub.write(opt1, centerX - 5 - border - footer - obfhub.chkLen(opt1) / 2, footpos, 16777215);
                obfhub.write(opt2, centerX + border + 5 + footer - obfhub.chkLen(opt2) / 2, footpos, 16777215);
            }
            else
            {
                if (this.iMenu != 4)
                    obfhub.write(opt1, centerX - 5 - border * 2 - footer * 2 - obfhub.chkLen(opt1) / 2, footpos, 16777215);

                obfhub.write(opt2, centerX - obfhub.chkLen(opt2) / 2, footpos, 16777215);

                if (this.iMenu != 4)
                    obfhub.write(opt3, centerX + 5 + border * 2 + footer * 2 - obfhub.chkLen(opt3) / 2, footpos, 16777215);
            }
        }

        if (this.iMenu > 4)
        {
            String verify = " !\"#$%&'()*+,-./0123456789;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}~⌂ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»";

            if (this.iMenu > 5 && this.inStr.equals(""))
                verify = "-0123456789n";
            else if (this.iMenu > 5) verify = "0123456789n";

            if (Keyboard.getEventKeyState())
            {
                do
                {
                    if (Keyboard.getEventKey() == Keyboard.KEY_RETURN && this.lastKey != Keyboard.KEY_RETURN)
                        if (this.inStr.equals(""))
                            this.next = 3;
                        else if (this.iMenu == 5)
                        {
                            this.next = 6;
                            this.way = this.inStr;
                            this.inStr = (conf.netherpoints ? "n" : "") + Integer.toString(obfhub.playerXCoord());
                        }
                        else if (this.iMenu == 6)
                        {
                            this.next = 7;

                            try
                            {
                                this.wayX = toInt(this.inStr);
                            }
                            catch (Exception localException)
                            {
                                this.next = 3;
                            }

                            this.inStr = (conf.netherpoints ? "n" : "") + Integer.toString(obfhub.playerZCoord());
                        }
                        else
                        {
                            this.next = 3;

                            try
                            {
                                this.wayZ = toInt(this.inStr);
                            }
                            catch (Exception localException)
                            {
                                this.inStr = "";
                            }

                            if (!this.inStr.equals(""))
                            {
                                conf.wayPts.add(new Waypoint(this.way, wayX, wayZ, true));
                                conf.saveWaypoints();

                                if (conf.wayPts.size() > 9) min = conf.wayPts.size() - 9;
                            }
                        }
                    else if (Keyboard.getEventKey() == Keyboard.KEY_BACK && this.lastKey != Keyboard.KEY_BACK)
                        if (this.inStr.length() > 0)
                            this.inStr = this.inStr.substring(0, this.inStr.length() - 1);

                    if (verify.indexOf(Keyboard.getEventCharacter()) >= 0 && Keyboard.getEventKey() != this.lastKey)
                        if (obfhub.chkLen(this.inStr + Keyboard.getEventCharacter()) < 148)
                            this.inStr = this.inStr + Keyboard.getEventCharacter();

                    this.lastKey = Keyboard.getEventKey();
                }
                while (Keyboard.next());
            }
            else
                this.lastKey = 0;

            GL11.glDisable(3553);
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);
            leftX = centerX - 75 - border;
            rightX = centerX + 75 + border;
            topY = centerY - 10 - border;
            botY = centerY + 10 + border;
            this.drawBox(leftX, rightX, topY, botY);
            leftX = leftX + border;
            rightX = rightX - border;
            topY = topY + 11;
            botY = botY - border;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            this.drawBox(leftX, rightX, topY, botY);
            GL11.glEnable(3553);
            String out = "Please enter a name:";

            if (this.iMenu == 6)
                out = "Enter X coordinate:";
            else if (this.iMenu == 7) out = "Enter Z coordinate:";

            obfhub.write(out, (int)leftX + border, (int)topY - 11 + border, 0xffffff);

            if (this.blink > 60) this.blink = 0;

            if (this.blink < 30)
                obfhub.write(this.inStr + "|", (int)leftX + border, (int)topY + border, 0xffffff);
            else
                obfhub.write(this.inStr, (int)leftX + border, (int)topY + border, 0xffffff);

            if (this.iMenu == 6)
                try
                {
                    if (toInt(this.inStr) == obfhub.playerXCoord() * (conf.netherpoints ? 8 : 1))
                        obfhub.write("(Current)", (int)leftX + border + obfhub.chkLen(this.inStr) + 5, (int)topY + border, 0xa0a0a0);
                }
                catch (Exception localException)
                {}
            else if (this.iMenu == 7)
                try
                {
                    if (toInt(this.inStr) == obfhub.playerZCoord() * (conf.netherpoints ? 8 : 1))
                        obfhub.write("(Current)", (int)leftX + border + obfhub.chkLen(this.inStr) + 5, (int)topY + border, 0xa0a0a0);
                }
                catch (Exception localException)
                {}

            this.blink++;
        }

        if (this.next != 0)
        {
            this.iMenu = this.next;
            this.next = 0;
        }
    }
    
    private void drawOptions(double rightX, double topY, int MouseX, int MouseY, boolean leftclick, boolean rightclick, boolean middleclick)
    {
        if (this.iMenu > 2)
        {
            if (min < 0) min = 0;

            if (!Mouse.isButtonDown(0) && scrollClick) scrollClick = false;

            if (MouseX > (rightX - 10) && MouseX < (rightX - 2) && MouseY > (topY + 1) && MouseY < (topY + 10))
            {
                if (leftbtndown)
                {
                    if (leftclick && min > 0) min--;

                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.7f);
                }
                else
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            }
            else
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);

            obfhub.draw_startQuads();
            obfhub.ldraw_setColor(rightX - 10, topY + 10, 0.0D);
            obfhub.ldraw_setColor(rightX - 2, topY + 10, 0.0D);
            obfhub.ldraw_setColor(rightX - 6, topY + 1, 0.0D);
            obfhub.ldraw_setColor(rightX - 6, topY + 1, 0.0D);
            obfhub.draw_finish();

            if (conf.wayPts.size() > 9)
            {
                sMax = (int)(9.0D / conf.wayPts.size() * 67.0D);
            }
            else
            {
                sMin = 0;
                sMax = 67;
            }

            if (MouseX > rightX - 10 && MouseX < rightX - 2 && MouseY > topY + 12 + sMin && MouseY < topY + 12 + sMin + sMax || scrollClick)
            {
                if (Mouse.isButtonDown(0) && !scrollClick)
                {
                    scrollClick = true;
                    scrStart = MouseY;
                }
                else if (scrollClick && conf.wayPts.size() > 9)
                {
                    int offset = MouseY - scrStart;

                    if (sMin + offset < 0)
                        sMin = 0;
                    else if (sMin + offset + sMax > 67)
                        sMin = 67 - sMax;
                    else
                    {
                        sMin = sMin + offset;
                        scrStart = MouseY;
                    }

                    min = (int)((sMin / (67.0D - sMax)) * (conf.wayPts.size() - 9));
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.7f);
                }
                else
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            }
            else
            {
                if (conf.wayPts.size() > 9)
                    sMin = (int)((double)min / (double)(conf.wayPts.size() - 9) * (67.0D - sMax));

                GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
            }

            this.drawBox(rightX - 10, rightX - 2, topY + 12 + sMin, topY + 12 + sMin + sMax);

            if (MouseX > rightX - 10 && MouseX < rightX - 2 && MouseY > topY + 81 && MouseY < topY + 90)
            {
                if (leftbtndown)
                {
                    if (leftclick && min < conf.wayPts.size() - 9) min++;

                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.7f);
                }
                else
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            }
            else
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);

            obfhub.draw_startQuads();
            obfhub.ldraw_setColor(rightX - 6, topY + 90, 0.0D);
            obfhub.ldraw_setColor(rightX - 6, topY + 90, 0.0D);
            obfhub.ldraw_setColor(rightX - 2, topY + 81, 0.0D);
            obfhub.ldraw_setColor(rightX - 10, topY + 81, 0.0D);
            obfhub.draw_finish();
        }

        double leftX = rightX - 30;
        double botY = 0;
        topY += 1;
        int max = min + 9;

        if (max > conf.wayPts.size())
        {
            max = conf.wayPts.size();

            if (min > 0)
            {
                if (max - 9 > 0)
                    min = max - 9;
                else
                    min = 0;
            }
        }

        double leftCl = 0;
        double rightCl = 0;

        if (this.iMenu > 2)
        {
            leftX = leftX - 14;
            rightX = rightX - 14;
            rightCl = rightX - 32;
            leftCl = rightCl - 9;
        }
        else
        {
            min = 0;
            max = mmOptCount;
        }

        for (int i = min; i < max; i++)
        {
            if (i > min) topY += 10;

            botY = topY + 9;

            if (MouseX > leftX && MouseX < rightX && MouseY > topY && MouseY < botY && this.iMenu < 5)
                if (leftbtndown)
                {
                    if (leftclick)
                    {
                        if (this.iMenu == 2)
                            this.setOptions(i);
                        else if (this.iMenu == 3)
                        {
                            conf.wayPts.get(i).enabled = !conf.wayPts.get(i).enabled;
                            conf.saveWaypoints();
                        }
                        else
                        {
                            this.delWay(i);
                            this.next = 3;
                        }
                    }

                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
                else
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.6f);
            else
            {
                if (this.iMenu == 2)
                {
                    if (this.chkOptions(i))
                        GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.6f);
                    else
                        GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.6f);
                }
                else if (this.iMenu == 4)
                {
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.4f);
                }
                else
                {
                    if (conf.wayPts.get(i).enabled)
                    {
                        GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.6f);
                    }
                    else
                        GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.6f);
                }
            }

            this.drawBox(leftX, rightX, topY, botY);

            if (iMenu > 2 && !(iMenu == 4 && this.next == 3))
            {
                if (MouseX > leftCl && MouseX < rightCl && MouseY > topY && MouseY < botY && this.iMenu == 3)
                    if (leftclick || rightclick)
                    {
                        Waypoint waypoint = conf.wayPts.get(i);
                        int color24 = ((int)(waypoint.red * 0xff) << 16) + ((int)(waypoint.green * 0xff) << 8) + ((int)(waypoint.blue * 0xff));
                        int index = conf.colorsequence.indexOf(color24);
                        int direction = 0;
                        if (leftclick)
                            direction = 1;
                        else if (rightclick)
                            direction = -1;
                        index = (index + direction) % conf.colorsequence.size();
                        if (index < 0)
                            index += conf.colorsequence.size();
                        color24 = conf.colorsequence.get(index);
                        waypoint.red = (float)((color24 & 0xff0000) >> 16) / (float)0xff;
                        waypoint.green = (float)((color24 & 0xff00) >> 8) / (float)0xff;
                        waypoint.blue = (float)(color24 & 0xff) / (float)0xff;
                        conf.saveWaypoints();
                    } else if (middleclick) {
                        Waypoint waypoint = conf.wayPts.get(i);
                        waypoint.red = generator.nextFloat();
                        waypoint.green = generator.nextFloat();
                        waypoint.blue = generator.nextFloat();
                        conf.saveWaypoints();
                    }

                GL11.glColor3f(conf.wayPts.get(i).red, conf.wayPts.get(i).green, conf.wayPts.get(i).blue);
                this.drawBox(leftCl, rightCl, topY, botY);
            }
        }
    }

    private void delWay(int i)
    {
        conf.wayPts.remove(i);
        conf.saveWaypoints();
    }

    private int drawFooter(int centerX, int centerY, int m, String opt1, String opt2, String opt3, int border, int MouseX, int MouseY, boolean leftclick, boolean rightclick, boolean middleclick)
    {
        int footer = obfhub.chkLen(opt1);

        if (obfhub.chkLen(opt2) > footer) footer = obfhub.chkLen(opt2);

        double leftX = centerX - footer - border * 2 - 5;
        double rightX = centerX - 5;
        double topY = centerY + (m - 1) / 2.0D * 10.0D - border + 10.0D;
        double botY = centerY + (m - 1) / 2.0D * 10.0D + border + 20.0D;

        if (this.iMenu > 2)
        {
            if (obfhub.chkLen(opt3) > footer) footer = obfhub.chkLen(opt3);

            leftX = centerX - border * 3 - footer * 1.5 - 5;
            rightX = centerX - footer / 2 - border - 5;
        }

        if (MouseX > leftX && MouseX < rightX && MouseY > topY && MouseY < botY && this.iMenu < 4)
            if (leftbtndown)
            {
                if (leftclick)
                {
                    if (this.iMenu == 2)
                        obfhub.setMenuNull();
                    else
                        this.next = 2;
                }

                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
            else
                GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.7f);
        else
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);

        if (this.iMenu != 4) this.drawBox(leftX, rightX, topY, botY);

        if (this.iMenu == 2)
        {
            leftX = centerX + 5;
            rightX = centerX + footer + border * 2 + 5;
        }
        else
        {
            leftX = centerX - footer / 2 - border;
            rightX = centerX + footer / 2 + border;
        }

        if (MouseX > leftX && MouseX < rightX && MouseY > topY && MouseY < botY && this.iMenu < 5)
            if (leftbtndown)
            {
                if (leftclick)
                {
                    if (this.iMenu == 2 || this.iMenu == 4)
                        this.next = 3;
                    else
                    {
                        this.next = 5;
                        this.inStr = "";
                    }
                }

                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }
            else
                GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.7f);
        else
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);

        this.drawBox(leftX, rightX, topY, botY);

        if (this.iMenu > 2)
        {
            rightX = centerX + border * 3 + footer * 1.5 + 5;
            leftX = centerX + footer / 2 + border + 5;

            if (MouseX > leftX && MouseX < rightX && MouseY > topY && MouseY < botY && this.iMenu < 4)
                if (leftbtndown)
                {
                    if (leftclick) this.next = 4;

                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
                else
                    GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.7f);
            else
                GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.7f);

            if (this.iMenu != 4) this.drawBox(leftX, rightX, topY, botY);
        }

        return footer / 2;
    }

    private boolean chkOptions(int i)
    {
        if (i == 0)      return conf.coords;
        else if (i == 1) return conf.hide;
        else if (i == 2) return conf.lightmap;
        else if (i == 3) return conf.heightmap;
        else if (i == 4) return conf.squaremap;
        else if (i == 5) return conf.welcome;
        else if (i == 6) return conf.threading;
        else if (i == 7) return conf.color;
        else if (i == 8) return conf.netherpoints;
        else if (i == 9) return conf.cavemap;
        throw new IllegalArgumentException("bad option number " + i);
    }

    private void setOptions(int i)
    {
        if (i == 0)
            conf.coords = !conf.coords;
        else if (i == 1)
            conf.hide = !conf.hide;
        else if (i == 2)
        {
            conf.lightmap = !conf.lightmap;
            if(conf.cavemap)
                conf.heightmap = !conf.lightmap;
        }
        else if (i == 3)
        {
            conf.heightmap = !conf.heightmap;
            if(conf.cavemap)
                conf.lightmap = !conf.heightmap;
        }
        else if (i == 4)
            conf.squaremap = !conf.squaremap;
        else if (i == 5)
            conf.welcome = !conf.welcome;
        else if (i == 6)
            conf.threading = !conf.threading;
        else if (i == 7)
        {
            if(!conf.cavemap)
                conf.color = !conf.color;
        }
        else if (i == 8)
            conf.netherpoints = !conf.netherpoints;
        else if (i == 9)
        {
            conf.cavemap = !conf.cavemap;
            if(conf.cavemap)
            {
                conf.lightmap = true;
                conf.heightmap = false;
                conf.full = false;
                conf.zoom=1;
                this.error = "Cavemap zoom (2.0x)";
            }
        }
        else
            throw new IllegalArgumentException("bad option number " + i);
        conf.saveAll();
        mapcalc.timer = 50000;

    }

    private void drawBox(double leftX, double rightX, double topY, double botY)
    {
        obfhub.draw_startQuads();
        obfhub.ldraw_setColor(leftX, botY, 0.0D);
        obfhub.ldraw_setColor(rightX, botY, 0.0D);
        obfhub.ldraw_setColor(rightX, topY, 0.0D);
        obfhub.ldraw_setColor(leftX, topY, 0.0D);
        obfhub.draw_finish();
    }
    
    void tick(int scWidth, int scHeight) {


        if ((!error.equals("")) && (ztimer == 0)) ztimer = 500;

        if (ztimer > 0) ztimer -= 1;

        if (fudge > 0) fudge -= 1;

        if ((ztimer == 0) && (!error.equals(""))) error = "";

        if (conf.enabled)
        {
            if (ztimer > 0) obfhub.write(error, 20, 20, 0xffffff);
    
            showMenu(scWidth, scHeight);
        }
    }
}
