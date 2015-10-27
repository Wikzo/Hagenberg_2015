using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ComponentFramework
{
    public class GameObjectList : ICollection<GameObject>, IEnumerable<GameObject>
    {
        private List<GameObject> _gameObjectList = new List<GameObject>();

        public GameObject this[int i]
        {
            get
            {
                return _gameObjectList[i];
            }
            set
            {
                _gameObjectList[i] = value;
            }
        }

        public void Add(GameObject item)
        {
            GameObject.AddToList(_gameObjectList, item);
        }

        public void AddRange(List<GameObject> listToAppend)
        {
            _gameObjectList.AddRange(listToAppend);
        }

        public void AddRange(GameObjectList listToAppend)
        {
            for (int i = 0; i < listToAppend.Count; i++)
            {
                _gameObjectList.Add(listToAppend[i]);
            }
        }

        public void Clear()
        {
            for (int i = 0; i < _gameObjectList.Count; i++)
            {
                GameObject.RemoveFromList(_gameObjectList, _gameObjectList[i]);
            }

            _gameObjectList.Clear();
        }

        public bool Contains(GameObject item)
        {
            return _gameObjectList.Contains(item);
        }

        public void CopyTo(GameObject[] array, int arrayIndex)
        {
            _gameObjectList.CopyTo(array, arrayIndex);
        }

        public int Count
        {
            get { return _gameObjectList.Count; }
        }

        public bool IsReadOnly
        {
            get { return false; }
        }

        public bool Remove(GameObject item)
        {
            return GameObject.RemoveFromList(_gameObjectList, item);
        }

        public IEnumerator<GameObject> GetEnumerator()
        {
            return _gameObjectList.GetEnumerator();
        }

        System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
        {
            return _gameObjectList.GetEnumerator();
        }

        public void Sort(Comparison<GameObject> comparison)
        {
            _gameObjectList.Sort(comparison);
        }

        
    }
}
