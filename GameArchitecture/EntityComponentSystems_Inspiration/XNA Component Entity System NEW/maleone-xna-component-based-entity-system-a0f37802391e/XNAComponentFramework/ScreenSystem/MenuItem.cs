using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using ComponentFramework;

namespace ComponentFramework.ScreenSystem
{
    public class MenuItem 
    {
        #region Fields

        public delegate void EntryStatusChanged(MenuItem item);

        string _text = String.Empty;
        Texture2D _texture = null;

        float _selectionFade;
        float _scale = 1f;
        float _rotation = 0f;

        public Vector2 Position;
        public Vector2 TextOffset = Vector2.Zero;

        public Color TextColor = Color.White;
        public Color TextureColor = Color.White;

        public Rectangle EntryHitBounds = new Rectangle();

        #endregion

        #region Properties

        public string Text
        {
            get { return _text; }
            set { _text = value; }
        }


        public Texture2D Texture
        {
            get { return _texture; }
            set { _texture = value; }
        }

        public float Scale
        {
            get { return _scale; }
            set { _scale = value; }
        }

        public float Rotation
        {
            get { return _rotation; }
            set { _rotation = value; }
        }

        #endregion

        #region Events

        public event EntryStatusChanged Selected;

        protected internal virtual void OnEntrySelected()
        {
            if (Selected != null)
            {
                Selected(this);
            }
        }

        public event EntryStatusChanged HighLighted;

        protected internal virtual void OnEntryHighLighted()
        {
            if (HighLighted != null)
	        {
                HighLighted(this);
	        }
        }

        public event EntryStatusChanged UnHighlighted;

        protected internal virtual void OnEntryUnHighlighted()
        {
            if (UnHighlighted != null)
            {
                UnHighlighted(this);
            }
        }

        #endregion

        #region Initialization

        public MenuItem(Vector2 aPosition)
        {
            this.Position = aPosition;
        }

        public MenuItem(Vector2 aPosition, string aText)
            : this(aPosition)
        {
            this._text = aText;

            EntryHitBounds = new Rectangle((int)this.Position.X, (int)this.Position.Y, this.GetTextWidth(), this.GetTextHeight());
        }

        public MenuItem(Vector2 aPosition, Texture2D aTexture)
            : this(aPosition)
        {
            this._texture = aTexture;

            EntryHitBounds = new Rectangle((int)this.Position.X, (int)this.Position.Y, this.Texture.Width, this.Texture.Height);
        }

        public MenuItem(Vector2 aPosition, string aText, Texture2D aTexture, Rectangle aEntryHitBounds)
            : this(aPosition, aText)
        {
            this._texture = aTexture;
            this.EntryHitBounds = aEntryHitBounds;
        }

        #endregion

        #region Update and Draw

        public virtual void Update(MenuScreen aScreen, bool aIsSelected)
        {
            float fadeSpeed = (float)TimeManager.ElapsedGameTime.TotalSeconds * 16;

            if (aIsSelected)
            {
                _selectionFade = Math.Min(_selectionFade + fadeSpeed, 1);
            }
            else
            {
                _selectionFade = Math.Max(_selectionFade - fadeSpeed, 0);
            }
        }

        public virtual void Draw(SpriteBatch aSpriteBatch, MenuScreen aScreen, bool aIsSelected, float aAlpha)
        {
            //Vector2 origin = new Vector2(0, font.LineSpacing / 2);

            this.TextColor = aIsSelected ? Color.Yellow : Color.White;
            this.TextColor *= aAlpha;

            if (this._text != String.Empty)
            {
                //draw the text
                //aSpriteBatch.DrawString(ScreenManager.MenuFont, _text, Position + TextOffset, color);
                aSpriteBatch.DrawString(ScreenManager.MenuFont, _text, Position, this.TextColor, _rotation, -TextOffset, _scale, SpriteEffects.None, 0f);
            }

            if (this._texture != null)
            {
                //draw the texture underneath the text
                aSpriteBatch.Draw(_texture, this.Position, this.TextureColor);
            }
        }

        #endregion

        #region Public Methods

        public virtual int GetTextHeight()
        {
            //return the sprite font's linespacing property
            return ScreenManager.MenuFont.LineSpacing;
        }

        public virtual int GetTextWidth()
        {
            return (int)ScreenManager.MenuFont.MeasureString(Text).X;
        }

        #endregion

    }
}
