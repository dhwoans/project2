<template>
  <div class="container">
    <masonry-wall
      :items="artwork_list"
      :ssr-columns="1"
      :column-width="300"
      :gap="16"
    >
      <template #default="{ item }">
        <div class="thumbnail-wrapper">
          <img :src="item.image" :alt="`${item.title}`" />
          <div class="overlay">
            <div class="info">
              <router-link
                :to="{ name: 'ArtworkDetail', params: { id: item.artworkId } }"
              >
                <span style="color: white" class="artwork-title">{{
                  item.title
                }}</span>
              </router-link>
              <router-link
                :to="{ name: 'Profile', params: { id: item.memberId } }"
              >
                <span style="color: white" class="artwork-artist"
                  >By {{ item.nickname }}</span
                >
              </router-link>
            </div>
            <div class="button-top-right">
              <like-button :liked="!valid" @click="handleLike"></like-button>
            </div>
          </div>
        </div>
      </template>
    </masonry-wall>
  </div>
  <loader></loader>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import LikeButton from "../common/LikeButton.vue";
import ArtworkAPI from "@/apis/artworkAPI";
import Loader from "@/components/main/child/Loader.vue";

export default defineComponent({
  data() {
    return {
      artwork_list: [] as any,
      valid: true,
      page: 0,
    };
  },
  components: {
    LikeButton,
    Loader,
  },
  methods: {
    handleLike() {
      this.valid = !this.valid;
    },
    async getArtwork() {
      let new_artworks = await ArtworkAPI.getArtworkList(this.page);
      let temp = new_artworks.data;
      console.log(temp);
      const new_artwork = [];
      let size = Object.keys(temp).length;
      for (let i = 0; i < size; i++) {
        new_artwork.push({
          artworkId: temp[i].artworkId,
          title: temp[i].artworkTitle,
          memberId: temp[i].memberId,
          nickname: temp[i].memberNickname,
          image: temp[i].saveFolder,
        });
      }
      this.artwork_list = [...this.artwork_list, ...new_artwork];
    },
    // 무한스크롤 수정
    handleScroll() {
      if (
        window.scrollY + window.innerHeight >=
        document.body.scrollHeight - 50
      ) {
        this.getArtwork();
      }
    },
    // handleScroll() {
    //   const observer = new IntersectionObserver((entries) => {
    //     entries.forEach(entry => {
    //       if(entry.intersectionRatio > 0 && this.currentPage < this.pageCount) {
    //         this.showloader = true;
    //         setTimeout(() => {
    //           this.currentPage += 1;
    //           this.showloader = false;
    //         }, 2000); // simulate Ajax-Call;
    //       }
    //     });
    //   });

    //   observer.observe(this.$refs.infinitescrolltrigger);
    // },
  },
  mounted() {
    // this.scrollTrigger();
    this.getArtwork();
    // window.addEventListener("scroll", this.handleScroll);
  },
  computed: {},
});
</script>

<style scoped lang="scss">
footer {
  position: relative;
  width: 400px;

  #scroll-trigger {
    height: 100px;
  }
}

* {
  box-sizing: border-box;
}

.thumbnail-wrapper {
  position: relative;
  width: 300px;
  margin: 0 auto;
  transition: 0.5s ease-in-out;

  & img {
    max-width: 100%;
    height: auto;
    border: 1px solid $grey;
    box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.5);
  }

  &:hover {
    transform: translateY(-3px);
    & .button-top-right {
      visibility: visible;
    }
    & .info {
      color: white;
      visibility: visible;
      transform: translateY(0px);
    }
  }
}

.info {
  position: absolute;
  bottom: $font-small;
  left: 10px;
  visibility: hidden;
  z-index: 3;
  transition: 0.4s, ease-in-out;
}

.container {
  margin-top: 2rem;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 2;
  &:hover {
    background: rgba(0, 0, 0, 0.6);
  }
}

.button-top-right {
  position: absolute;
  top: 2px;
  right: 2px;
  z-index: 3;
  visibility: hidden;
}
</style>
